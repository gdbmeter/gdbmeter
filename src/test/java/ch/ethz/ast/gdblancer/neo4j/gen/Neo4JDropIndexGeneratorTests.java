package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Neo4JDropIndexGeneratorTests {

    @Test
    void testDropIndex() {
        try {
            String query = Neo4JDropIndexGenerator.dropIndex(Neo4JDBSchema.generateRandomSchema());

            assertNotNull(query);
            assertFalse(query.isEmpty());
            assertTrue(query.startsWith("DROP INDEX"));
        } catch (IgnoreMeException exception) {

        }
    }

}
