package ch.ethz.ast.gdblancer.neo4j.gen.schema;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class Neo4JDBEntityTests {

    @Test
    void testGenerateRandomEntity() {
        Neo4JDBEntity entity = Neo4JDBEntity.generateRandomEntity(Neo4JType.values());
        Map<String, Neo4JType> availableProperties = entity.getAvailableProperties();

        assertNotNull(entity);
        assertNotNull(availableProperties);
        assertFalse(availableProperties.isEmpty());
    }

}
