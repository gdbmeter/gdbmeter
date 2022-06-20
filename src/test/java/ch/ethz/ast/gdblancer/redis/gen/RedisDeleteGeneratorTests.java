package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RedisDeleteGeneratorTests {

    @Test
    void testDeleteNodes() {
        while (true) {
            try {
                CypherSchema schema = CypherSchema.generateRandomSchema(RedisExpressionGenerator.supportedTypes);
                Query<?> query = RedisDeleteGenerator.deleteNodes(schema);

                assertNotNull(query);
                assertTrue(query.getQuery().startsWith("MATCH "));
                assertTrue(query.getQuery().endsWith(" DELETE n"));
                break;
            } catch (IgnoreMeException ignored) {}
        }
    }

}
