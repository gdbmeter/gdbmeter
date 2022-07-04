package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.neo4j.schema.Neo4JType;
import ch.ethz.ast.gdblancer.redis.schema.RedisType;

import java.util.Set;

public class RedisSchemaGenerator {

    Schema<RedisType> makeSchema() {
        return Schema.generateRandomSchema(Set.of(RedisType.values()));
    }

}
