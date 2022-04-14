package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBEntity;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Neo4JPropertyGeneratorTests {

    @Test
    void testCreatePropertyQuery() {
        try {
            Neo4JDBEntity entity = Neo4JDBEntity.generateRandomEntity();

            String query = Neo4JPropertyGenerator.generatePropertyQuery(entity);
            assertNotNull(query);
        } catch (IgnoreMeException ignored) {

        }
    }

}
