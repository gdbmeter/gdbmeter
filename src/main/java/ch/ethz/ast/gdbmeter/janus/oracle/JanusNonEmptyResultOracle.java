package ch.ethz.ast.gdbmeter.janus.oracle;

import ch.ethz.ast.gdbmeter.common.GlobalState;
import ch.ethz.ast.gdbmeter.common.Oracle;
import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.janus.JanusConnection;
import ch.ethz.ast.gdbmeter.janus.query.JanusQuery;
import ch.ethz.ast.gdbmeter.janus.schema.JanusType;
import ch.ethz.ast.gdbmeter.janus.schema.Predicate;
import ch.ethz.ast.gdbmeter.janus.schema.PredicateGenerator;
import ch.ethz.ast.gdbmeter.janus.schema.PredicateVisitor;
import ch.ethz.ast.gdbmeter.util.IgnoreMeException;
import ch.ethz.ast.gdbmeter.util.Randomization;
import org.apache.tinkerpop.gremlin.structure.T;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JanusNonEmptyResultOracle implements Oracle {

    private final GlobalState<JanusConnection> state;
    private final Schema<JanusType> schema;

    public JanusNonEmptyResultOracle(GlobalState<JanusConnection> state, Schema<JanusType> schema) {
        this.state = state;
        this.schema = schema;
    }

    @Override
    public void check() {
        Set<Long> allIds = new HashSet<>();
        List<Map<String, Object>> idResult = new JanusQuery("g.V().valueMap(true).toList()").executeAndGet(state);

        for (Map<String, Object> properties : idResult) {
            //noinspection SuspiciousMethodCalls
            allIds.add((Long) properties.get(T.id));
        }
        
        String label = schema.getRandomLabel();
        Entity<JanusType> entity = schema.getEntityByLabel(label);
        Map<String, JanusType> availableProperties = entity.getAvailableProperties();

        String matchProperty = Randomization.fromSet(availableProperties.keySet());
        JanusType matchType = availableProperties.get(matchProperty);
        Predicate predicate = PredicateGenerator.generateFor(matchType);

        String query = String.format("g.V().hasLabel('%s').has('%s', %s).valueMap(true).toList()",
                label,
                matchProperty,
                PredicateVisitor.asString(predicate));
        JanusQuery initialQuery = new JanusQuery(query);
        List<Map<String, Object>> initialResult = initialQuery.executeAndGet(state);

        if (initialResult == null) {
            throw new IgnoreMeException();
        }

        int initialSize = initialResult.size();

        if (initialSize != 0) {
            for (Map<String, Object> properties : initialResult) {
                //noinspection SuspiciousMethodCalls
                allIds.remove((Long) properties.get(T.id));
            }

            if (allIds.isEmpty()) {
                throw new IgnoreMeException();
            }

            for (int i = 0; i < Randomization.smallNumber() && !allIds.isEmpty(); i++) {
                Long chosenId = Randomization.fromSet(allIds);

                new JanusQuery(String.format("g.V(%d).drop().iterate()", chosenId)).execute(state);
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

}
