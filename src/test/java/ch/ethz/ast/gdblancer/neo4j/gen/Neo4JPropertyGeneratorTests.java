package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Neo4JPropertyGeneratorTests {

    @Test
    void testCreatePropertyQuery() {
        Neo4JDBEntity entity = Neo4JDBEntity.generateRandomEntity();

        String query = Neo4JPropertyGenerator.generatePropertyQuery(entity, true);
        assertNotNull(query);

        query = Neo4JPropertyGenerator.generatePropertyQuery(entity, false);
        assertNotNull(query);
    }

}
