package ch.ethz.ast.gdblancer.redis.oracle;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Oracle;
import ch.ethz.ast.gdblancer.cypher.ast.CypherExpression;
import ch.ethz.ast.gdblancer.cypher.ast.CypherPrefixOperation;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.cypher.schema.CypherType;
import ch.ethz.ast.gdblancer.redis.RedisConnection;
import ch.ethz.ast.gdblancer.redis.RedisQuery;
import ch.ethz.ast.gdblancer.redis.RedisUtil;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;

import java.util.List;
import java.util.Map;

public class RedisPartitionOracle implements Oracle {

    private final GlobalState<RedisConnection> state;
    private final CypherSchema schema;

    public RedisPartitionOracle(GlobalState<RedisConnection> state, CypherSchema schema) {
        this.state = state;
        this.schema = schema;
    }

    @Override
    public void check() {
        int exceptions = 0;
        ExpectedErrors errors = new ExpectedErrors();

        RedisUtil.addFunctionErrors(errors);
        RedisUtil.addArithmeticErrors(errors);

        String label = schema.getRandomLabel();
        CypherEntity entity = schema.getEntityByLabel(label);

        RedisQuery initialQuery = new RedisQuery(String.format("MATCH (n:%s) RETURN COUNT(n)", label));
        List<Map<String, Object>> result = initialQuery.executeAndGet(state);
        Long expectedTotal = 0L;

        if (result != null) {
            expectedTotal = (Long) result.get(0).get("COUNT(n)");
        } else {
            throw new AssertionError("Unexpected exception when fetching total");
        }

        StringBuilder query = new StringBuilder();
        query.append(String.format("MATCH (n:%s)", label));
        query.append(" WHERE ");

        CypherExpression whereCondition = RedisExpressionGenerator.generateExpression(Map.of("n", entity), CypherType.BOOLEAN);
        query.append(CypherVisitor.asString(whereCondition));
        query.append(" RETURN COUNT(n)");

        RedisQuery firstQuery = new RedisQuery(query.toString(), errors);
        result = firstQuery.executeAndGet(state);
        Long first = 0L;

        if (result != null) {
            first = (Long) result.get(0).get("COUNT(n)");
        } else {
            exceptions++;
        }

        query = new StringBuilder();
        query.append(String.format("MATCH (n:%s)", label));
        query.append(" WHERE ");

        query.append(CypherVisitor.asString(new CypherPrefixOperation(whereCondition, CypherPrefixOperation.PrefixOperator.NOT)));
        query.append(" RETURN COUNT(n)");

        RedisQuery secondQuery = new RedisQuery(query.toString(), errors);
        result = secondQuery.executeAndGet(state);
        Long second = 0L;

        if (result != null) {
            second = (Long) result.get(0).get("COUNT(n)");
        } else {
            exceptions++;
        }

        query = new StringBuilder();
        query.append(String.format("MATCH (n:%s)", label));
        query.append(" WHERE (");

        query.append(CypherVisitor.asString(whereCondition));
        query.append(") IS NULL RETURN COUNT(n)");

        RedisQuery thirdQuery = new RedisQuery(query.toString(), errors);
        result = thirdQuery.executeAndGet(state);
        Long third = 0L;

        if (result != null) {
            third = (Long) result.get(0).get("COUNT(n)");
        } else {
            exceptions++;
        }

        if (exceptions > 0) {
            throw new IgnoreMeException();
        }

        if (first + second + third != expectedTotal) {
            throw new AssertionError(String.format("%d + %d + %d is not equal to %d", first, second, third, expectedTotal));
        }
    }

}
