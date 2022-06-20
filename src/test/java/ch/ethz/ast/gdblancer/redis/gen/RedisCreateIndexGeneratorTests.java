package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RedisCreateIndexGeneratorTests {

    @Test
    void testCreateIndex() {
        while (true) {
            try {
                CypherSchema schema = CypherSchema.generateRandomSchema(RedisExpressionGenerator.supportedTypes);
                assertNotNull(RedisCreateIndexGenerator.createIndex(schema));
                break;
            } catch (IgnoreMeException ignored) {}
        }
    }

}
