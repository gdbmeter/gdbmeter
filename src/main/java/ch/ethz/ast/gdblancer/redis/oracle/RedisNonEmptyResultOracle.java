package ch.ethz.ast.gdblancer.redis.oracle;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Oracle;
import ch.ethz.ast.gdblancer.cypher.ast.CypherExpression;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.cypher.schema.CypherType;
import ch.ethz.ast.gdblancer.redis.RedisConnection;
import ch.ethz.ast.gdblancer.redis.RedisQuery;
import ch.ethz.ast.gdblancer.redis.RedisUtil;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisNonEmptyResultOracle implements Oracle {

    private final GlobalState<RedisConnection> state;
    private final CypherSchema schema;

    public RedisNonEmptyResultOracle(GlobalState<RedisConnection> state, CypherSchema schema) {
        this.state = state;
        this.schema = schema;
    }

    private String randomMatch(String label) {
        CypherEntity entity = schema.getEntityByLabel(label);

        StringBuilder query = new StringBuilder();
        query.append(String.format("MATCH (n:%s)", label));
        query.append(" WHERE ");
        CypherExpression matchingExpression = RedisExpressionGenerator.generateExpression(Map.of("n", entity), CypherType.BOOLEAN);
        query.append(CypherVisitor.asString(matchingExpression));

        return query.toString();
    }

    @Override
    public void check() {
        ExpectedErrors errors = new ExpectedErrors();
        RedisUtil.addFunctionErrors(errors);

        Set<Long> allIds = new HashSet<>();
        List<Map<String, Object>> idResult = new RedisQuery("MATCH (n) RETURN id(n)").executeAndGet(state);

        for (Map<String, Object> properties : idResult) {
            allIds.add((Long) properties.get("id(n)"));
        }

        String label = schema.getRandomLabel();
        RedisQuery initialQuery = new RedisQuery(randomMatch(label) + " RETURN id(n)", errors);
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
                new RedisQuery(String.format("MATCH (n) WHERE id(n) = %d DETACH DELETE n", chosenId)).execute(state);
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
