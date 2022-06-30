package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;
import ch.ethz.ast.gdblancer.redis.schema.RedisType;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RedisSetGeneratorTests extends RedisSchemaGenerator {

    @Test
    void testSetProperties() {
        while (true) {
            try {
                Schema<RedisType> schema = makeSchema();
                Query<?> query = RedisSetGenerator.setProperties(schema);

                assertNotNull(query);
                assertTrue(query.getQuery().startsWith("MATCH "));
                assertTrue(query.getQuery().contains(" SET n"));
                break;
            } catch (IgnoreMeException ignored) {
            }
        }
    }

}
