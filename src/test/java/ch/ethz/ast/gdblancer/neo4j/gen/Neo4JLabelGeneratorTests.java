package ch.ethz.ast.gdblancer.neo4j.gen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Neo4JLabelGeneratorTests {

    @Test
    void testGenerateRandomLabel() {
        String query = Neo4JLabelGenerator.generateRandomLabel();

        assertNotNull(query);
        assertFalse(query.isEmpty());
        assertTrue(query.startsWith(":"));
        assertFalse(query.substring(1).isEmpty());
    }
}
