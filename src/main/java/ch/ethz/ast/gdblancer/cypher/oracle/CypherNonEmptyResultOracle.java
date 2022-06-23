package ch.ethz.ast.gdblancer.cypher.oracle;

import ch.ethz.ast.gdblancer.common.Connection;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Oracle;
import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class CypherNonEmptyResultOracle<T extends Connection> implements Oracle {

    private final GlobalState<T> state;
    private final CypherSchema schema;

    public CypherNonEmptyResultOracle(GlobalState<T> state, CypherSchema schema) {
        this.state = state;
        this.schema = schema;
    }

    @Override
    public void check() {
        Set<Long> allIds = new HashSet<>();
        List<Map<String, Object>> idResult = getIdQuery().executeAndGet(state);

        for (Map<String, Object> properties : idResult) {
            allIds.add((Long) properties.get("id(n)"));
        }

        String label = schema.getRandomLabel();
        CypherEntity entity = schema.getEntityByLabel(label);

        Query<T> initialQuery = getInitialQuery(label, entity);
        List<Map<String, Object>> initialResult = initialQuery.executeAndGet(state);

        if (initialResult == null) {
            throw new IgnoreMeException();
        }

        int initialSize = initialResult.size();

        if (initialSize != 0) {
            for (Map<String, Object> properties : initialResult) {
                allIds.remove((Long) properties.get("id(n)"));
            }

            if (allIds.isEmpty()) {
                throw new IgnoreMeException();
            }

            for (int i = 0; i < Randomization.smallNumber() && !allIds.isEmpty(); i++) {
                Long chosenId = Randomization.fromSet(allIds);

                getDeleteQuery(chosenId).execute(state);
                allIds.remove(chosenId);
            }

            List<Map<String, Object>> result = initialQuery.executeAndGet(state);

            if (result == null) {
                throw new AssertionError("Non empty oracle failed because the second query threw an exception");
            }

            if (result.size() != initialSize) {
                throw new AssertionError(String.format("Non empty oracle failed with size: %d, expected was %d", result.size(), initialResult.size()));
            }
        }
    }

    protected abstract Query<T> getIdQuery();
    protected abstract Query<T> getInitialQuery(String label, CypherEntity entity);
    protected abstract Query<T> getDeleteQuery(Long id);

}
