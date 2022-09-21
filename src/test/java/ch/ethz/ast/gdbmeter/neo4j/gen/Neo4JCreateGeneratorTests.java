package ch.ethz.ast.gdbmeter.neo4j.gen;

import ch.ethz.ast.gdbmeter.common.Query;
import ch.ethz.ast.gdbmeter.util.IgnoreMeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Neo4JCreateGeneratorTests extends Neo4JSchemaGenerator {

    @Test
    void testCreateEntities() {
        while (true) {
            try {
                Query<?> query = Neo4JCreateGenerator.createEntities(makeSchema());

                assertNotNull(query);
                assertTrue(query.getQuery().startsWith("CREATE") || query.getQuery().startsWith("MERGE"));
                break;
            } catch (IgnoreMeException ignored) {}
        }
    }

}
