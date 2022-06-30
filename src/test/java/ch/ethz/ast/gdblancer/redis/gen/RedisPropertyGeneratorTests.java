package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.common.schema.Entity;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;
import ch.ethz.ast.gdblancer.redis.schema.RedisType;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RedisPropertyGeneratorTests {

    @Test
    void testGenerateProperties() {
        while (true) {
            try {
                Entity<RedisType> entity = Entity.generateRandomEntity(Set.of(RedisType.values()));

                String query = new RedisPropertyGenerator(entity).generateProperties();
                assertNotNull(query);

                if (!query.isEmpty()) {
                    assertTrue(query.startsWith("{"));
                    assertTrue(query.endsWith("}"));
                }

                break;
            } catch (IgnoreMeException ignored) {}
        }
    }

}
