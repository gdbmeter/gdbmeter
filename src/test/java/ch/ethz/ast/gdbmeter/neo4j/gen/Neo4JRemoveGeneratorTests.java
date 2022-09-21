package ch.ethz.ast.gdbmeter.neo4j.gen;

import ch.ethz.ast.gdbmeter.common.Query;
import ch.ethz.ast.gdbmeter.util.IgnoreMeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Neo4JRemoveGeneratorTests extends Neo4JSchemaGenerator {

    @Test
    void testRemoveProperties() {
        while (true) {
            try {
                Query<?> query = Neo4JRemoveGenerator.removeProperties(makeSchema());

                assertNotNull(query);
                assertTrue(query.getQuery().startsWith("MATCH "));
                assertTrue(query.getQuery().contains(" REMOVE n"));
                break;
            } catch (IgnoreMeException ignored) {}
        }
    }

}
