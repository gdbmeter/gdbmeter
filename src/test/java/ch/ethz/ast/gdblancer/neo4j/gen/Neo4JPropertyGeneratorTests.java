package ch.ethz.ast.gdblancer.neo4j.gen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Neo4JPropertyGeneratorTests {

    @Test
    void testCreatePropertyQuery() {
        String query = Neo4JPropertyGenerator.generatePropertyQuery(true);
        assertNotNull(query);

        query = Neo4JPropertyGenerator.generatePropertyQuery(false);
        assertNotNull(query);
    }

}
