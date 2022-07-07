package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.redis.schema.RedisType;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RedisCreateIndexGeneratorTests extends RedisSchemaGenerator {

    @Test
    void testCreateIndex() {
        while (true) {
            try {
                Schema<RedisType> schema = makeSchema();
                assertNotNull(RedisCreateIndexGenerator.createIndex(schema));
                break;
            } catch (IgnoreMeException ignored) {}
        }
    }

}
