package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RedisRemoveGeneratorTests {

    @Test
    void testRemoveProperties() {
        while (true) {
            try {
                CypherSchema schema = CypherSchema.generateRandomSchema(RedisExpressionGenerator.supportedTypes);
                Query<?> query = RedisRemoveGenerator.removeProperties(schema);

                assertNotNull(query);
                assertTrue(query.getQuery().startsWith("MATCH "));
                assertTrue(query.getQuery().contains(" SET n."));
                assertTrue(query.getQuery().contains(" = NULL"));
                break;
            } catch (IgnoreMeException ignored) {}
        }
    }

}
