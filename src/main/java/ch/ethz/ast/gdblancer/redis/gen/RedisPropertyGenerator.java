package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.cypher.gen.CypherPropertyGenerator;
import ch.ethz.ast.gdblancer.cypher.ast.CypherExpression;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherType;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;

import java.util.Collections;
import java.util.Map;

public class RedisPropertyGenerator extends CypherPropertyGenerator {

    protected RedisPropertyGenerator(CypherEntity entity) {
        super(entity);
    }

    protected RedisPropertyGenerator(CypherEntity entity, Map<String, CypherEntity> variables) {
        super(entity, variables);
    }

    @Override
    protected CypherExpression generateConstant(CypherType type) {
        return RedisExpressionGenerator.generateConstant(type);
    }

    @Override
    protected CypherExpression generateExpression(Map<String, CypherEntity> variables, CypherType type) {
        // Accessing properties of nodes created in the same query is not supported by redis
        // See: https://github.com/RedisGraph/RedisGraph/pull/1495
        return RedisExpressionGenerator.generateExpression(Collections.emptyMap(), type);
    }

}
