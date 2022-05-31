package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.cypher.gen.CypherPropertyGenerator;
import ch.ethz.ast.gdblancer.neo4j.gen.ast.Neo4JExpression;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBEntity;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JType;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;

import java.util.Collections;
import java.util.Map;

public class RedisPropertyGenerator extends CypherPropertyGenerator {

    protected RedisPropertyGenerator(Neo4JDBEntity entity) {
        super(entity);
    }

    protected RedisPropertyGenerator(Neo4JDBEntity entity, Map<String, Neo4JDBEntity> variables) {
        super(entity, variables);
    }

    @Override
    protected Neo4JExpression generateConstant(Neo4JType type) {
        return RedisExpressionGenerator.generateConstant(type);
    }

    @Override
    protected Neo4JExpression generateExpression(Map<String, Neo4JDBEntity> variables, Neo4JType type) {
        // Accessing properties of nodes created in the same query is not supported by redis
        // See: https://github.com/RedisGraph/RedisGraph/pull/1495
        return RedisExpressionGenerator.generateExpression(Collections.emptyMap(), type);
    }

}
