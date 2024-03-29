package ch.ethz.ast.gdbmeter.redis.gen;

import ch.ethz.ast.gdbmeter.cypher.gen.CypherPropertyGenerator;
import ch.ethz.ast.gdbmeter.cypher.ast.CypherExpression;
import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.redis.ast.RedisExpressionGenerator;
import ch.ethz.ast.gdbmeter.redis.schema.RedisType;

import java.util.Collections;
import java.util.Map;

public class RedisPropertyGenerator extends CypherPropertyGenerator<RedisType> {

    protected RedisPropertyGenerator(Entity<RedisType> entity) {
        super(entity);
    }

    protected RedisPropertyGenerator(Entity<RedisType> entity, Map<String, Entity<RedisType>> variables) {
        super(entity, variables);
    }

    @Override
    protected CypherExpression generateConstant(RedisType type) {
        return RedisExpressionGenerator.generateConstant(type);
    }

    @Override
    protected CypherExpression generateExpression(Map<String, Entity<RedisType>> variables, RedisType type) {
        // Accessing properties of nodes created in the same query is not supported by redis
        // See: https://github.com/RedisGraph/RedisGraph/pull/1495
        return RedisExpressionGenerator.generateExpression(Collections.emptyMap(), type);
    }

}
