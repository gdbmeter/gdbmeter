package ch.ethz.ast.gdblancer.neo4j.gen.schema;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MongoDBEntityTests {

    @Test
    void testGenerateRandomEntity() {
        MongoDBEntity entity = MongoDBEntity.generateRandomEntity();
        Map<String, MongoDBPropertyType> availableProperties = entity.getAvailableProperties();

        assertNotNull(entity);
        assertNotNull(availableProperties);
        assertFalse(availableProperties.isEmpty());
    }

}
