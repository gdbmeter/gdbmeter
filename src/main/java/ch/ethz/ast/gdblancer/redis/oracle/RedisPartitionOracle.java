package ch.ethz.ast.gdblancer.redis.oracle;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.cypher.ast.CypherExpression;
import ch.ethz.ast.gdblancer.cypher.oracle.CypherPartitionOracle;
import ch.ethz.ast.gdblancer.common.schema.Entity;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.redis.RedisConnection;
import ch.ethz.ast.gdblancer.redis.RedisQuery;
import ch.ethz.ast.gdblancer.redis.RedisUtil;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;
import ch.ethz.ast.gdblancer.redis.schema.RedisType;

import java.util.Map;

public class RedisPartitionOracle extends CypherPartitionOracle<RedisConnection, RedisType> {

    private final ExpectedErrors errors = new ExpectedErrors();

    public RedisPartitionOracle(GlobalState<RedisConnection> state, Schema<RedisType> schema) {
        super(state, schema);

        RedisUtil.addFunctionErrors(errors);
        RedisUtil.addArithmeticErrors(errors);
    }

    @Override
    protected CypherExpression getWhereClause(Entity<RedisType> entity) {
        return RedisExpressionGenerator.generateExpression(Map.of("n", entity), RedisType.BOOLEAN);
    }

    @Override
    protected Query<RedisConnection> makeQuery(String query) {
        return new RedisQuery(query, errors);
    }

}
