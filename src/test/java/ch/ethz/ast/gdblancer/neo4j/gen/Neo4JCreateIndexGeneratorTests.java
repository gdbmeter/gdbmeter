package ch.ethz.ast.gdblancer.neo4j.gen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Neo4JCreateIndexGeneratorTests {

    @Test
    void testCreateIndex() {
        String query = Neo4JCreateIndexGenerator.createIndex();

        assertNotNull(query);
        assertFalse(query.isEmpty());
        assertTrue(query.startsWith("CREATE"));
    }

}
