package ch.ethz.ast.gdbmeter.redis.gen;

import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.redis.schema.RedisType;

import java.util.Set;

public class RedisSchemaGenerator {

    Schema<RedisType> makeSchema() {
        return Schema.generateRandomSchema(Set.of(RedisType.values()));
    }

}
