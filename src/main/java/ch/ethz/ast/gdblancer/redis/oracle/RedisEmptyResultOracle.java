package ch.ethz.ast.gdblancer.redis.oracle;

import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Oracle;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.cypher.schema.CypherType;
import ch.ethz.ast.gdblancer.redis.RedisConnection;
import ch.ethz.ast.gdblancer.redis.RedisQuery;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisEmptyResultOracle implements Oracle {

    private final GlobalState<RedisConnection> state;
    private final CypherSchema schema;

    public RedisEmptyResultOracle(GlobalState<RedisConnection> state, CypherSchema schema) {
        this.state = state;
        this.schema = schema;
    }

    @Override
    public void check() {
        Set<Long> allIds = new HashSet<>();
        List<Map<String, Object>> idResult = new RedisQuery("MATCH (n) RETURN id(n)").executeAndGet(state);

        for (Map<String, Object> properties : idResult) {
            allIds.add((Long) properties.get("id(n)"));
        }

        if (allIds.isEmpty()) {
            return;
        }

        String label = schema.getRandomLabel();
        CypherEntity entity = schema.getEntityByLabel(label);

        String query = String.format("MATCH (n:%s)", label) +
                " WHERE " +
                CypherVisitor.asString(RedisExpressionGenerator.generateExpression(Map.of("n", entity), CypherType.BOOLEAN)) +
                " RETURN n";

        RedisQuery initialQuery = new RedisQuery(query);
        List<Map<String, Object>> initialResult = initialQuery.executeAndGet(state);

        if (initialResult == null) {
            throw new IgnoreMeException();
        }

        if (initialResult.isEmpty()) {
            for (int i = 0; i < Randomization.smallNumber() && !allIds.isEmpty(); i++) {
                Long chosenId = Randomization.fromSet(allIds);
                new RedisQuery(String.format("MATCH (n) WHERE id(n) = %d DETACH DELETE n", chosenId)).execute(state);
                allIds.remove(chosenId);
            }

            List<Map<String, Object>> result = initialQuery.executeAndGet(state);

            if (result == null) {
                throw new AssertionError("Empty oracle failed because the second query threw an exception");
            }

            if (result.size() != 0) {
                throw new AssertionError(String.format("Empty oracle failed with size: %d", result.size()));
            }
        }
    }

}
