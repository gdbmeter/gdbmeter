package ch.ethz.ast.gdblancer.redis.oracle;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.cypher.ast.CypherExpression;
import ch.ethz.ast.gdblancer.cypher.oracle.CypherPartitionOracle;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.cypher.schema.CypherType;
import ch.ethz.ast.gdblancer.redis.RedisConnection;
import ch.ethz.ast.gdblancer.redis.RedisQuery;
import ch.ethz.ast.gdblancer.redis.RedisUtil;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;

import java.util.Map;

public class RedisPartitionOracle extends CypherPartitionOracle<RedisConnection> {

    private final ExpectedErrors errors = new ExpectedErrors();

    public RedisPartitionOracle(GlobalState<RedisConnection> state, CypherSchema schema) {
        super(state, schema);

        RedisUtil.addFunctionErrors(errors);
        RedisUtil.addArithmeticErrors(errors);
    }

    @Override
    protected CypherExpression getWhereClause(CypherEntity entity) {
        return RedisExpressionGenerator.generateExpression(Map.of("n", entity), CypherType.BOOLEAN);
    }

    @Override
    protected Query<RedisConnection> makeQuery(String query) {
        return new RedisQuery(query, errors);
    }

}
