package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Neo4JCreateIndexGeneratorTests {

    @Test
    void testCreateIndex() {
        try {
            Query<?> query = Neo4JCreateIndexGenerator.createIndex(CypherSchema.generateRandomSchema());

            assertNotNull(query);
            assertTrue(query.getQuery().startsWith("CREATE"));
        } catch (IgnoreMeException ignored) {}
    }

}
