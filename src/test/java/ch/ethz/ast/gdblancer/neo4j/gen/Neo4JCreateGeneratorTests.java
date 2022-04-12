package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Neo4JCreateGeneratorTests {

    @Test
    void testCreateEntities() {
        Query query = Neo4JCreateGenerator.createEntities(Neo4JDBSchema.generateRandomSchema());
        
        assertNotNull(query);
        assertTrue(query.getQuery().startsWith("CREATE") || query.getQuery().startsWith("MERGE"));
    }

}
