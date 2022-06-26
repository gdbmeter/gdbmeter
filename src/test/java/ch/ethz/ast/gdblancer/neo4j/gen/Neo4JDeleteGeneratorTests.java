package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.common.schema.CypherSchema;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Neo4JDeleteGeneratorTests {

    @Test
    void testDeleteNodes() {
        while (true) {
            try {
                Query<?> query = Neo4JDeleteGenerator.deleteNodes(CypherSchema.generateRandomSchema());

                assertNotNull(query);
                assertTrue(query.getQuery().startsWith("MATCH "));
                assertTrue(query.getQuery().contains(" DELETE n"));
                break;
            } catch (IgnoreMeException ignored) {}
        }
    }

}
