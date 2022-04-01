package ch.ethz.ast.gdblancer.neo4j.gen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Neo4JCreateGeneratorTests {

    @Test
    void testCreateEntities() {
        String query = Neo4JCreateGenerator.createEntities();
        
        assertNotNull(query);
        assertFalse(query.isEmpty());
        assertTrue(query.startsWith("CREATE"));
    }

}
