package ch.ethz.ast.gdblancer.neo4j.gen.schema;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MongoDBSchemaTests {

    @Test
    void testGenerateRandomSchema() {
        MongoDBSchema schema = MongoDBSchema.generateRandomSchema();
        String label = schema.getRandomLabel();
        String type = schema.getRandomType();

        assertNotNull(schema);

        assertNotNull(label);
        assertFalse(label.isEmpty());

        assertNotNull(type);
        assertFalse(type.isEmpty());

        String property = schema.getRandomPropertyForLabel(label);
        assertNotNull(label);
        assertFalse(property.isEmpty());

        property = schema.getRandomPropertyForRelationship(type);
        assertNotNull(label);
        assertFalse(property.isEmpty());
    }

    @Test
    void testGenerateIndexName() {
        MongoDBSchema schema = MongoDBSchema.generateRandomSchema();
        assertNotEquals(schema.generateIndexName(), schema.generateIndexName());
    }

}
