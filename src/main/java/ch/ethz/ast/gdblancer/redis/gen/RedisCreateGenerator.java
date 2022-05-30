package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.neo4j.gen.CypherCreateGenerator;
import ch.ethz.ast.gdblancer.neo4j.gen.CypherPropertyGenerator;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBEntity;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.redis.RedisQuery;

import java.util.Map;

public class RedisCreateGenerator extends CypherCreateGenerator {

    public RedisCreateGenerator(Neo4JDBSchema schema) {
        super(schema);
    }

    @Override
    protected CypherPropertyGenerator getPropertyGenerator(Neo4JDBEntity entity, Map<String, Neo4JDBEntity> variables) {
        return new RedisPropertyGenerator(entity, variables);
    }

    public static RedisQuery createEntities(Neo4JDBSchema schema) {
        RedisCreateGenerator generator = new RedisCreateGenerator(schema);
        generator.generateCreate();
        return new RedisQuery(generator.query.toString(), generator.errors);
    }

}
