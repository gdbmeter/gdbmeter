package ch.ethz.ast.gdbmeter.redis.gen;

import ch.ethz.ast.gdbmeter.common.Query;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.redis.schema.RedisType;
import ch.ethz.ast.gdbmeter.util.IgnoreMeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RedisRemoveGeneratorTests extends RedisSchemaGenerator {

    @Test
    void testRemoveProperties() {
        while (true) {
            try {
                Schema<RedisType> schema = makeSchema();
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
