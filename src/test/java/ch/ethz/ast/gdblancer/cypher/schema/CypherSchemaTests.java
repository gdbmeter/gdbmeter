package ch.ethz.ast.gdblancer.cypher.schema;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CypherSchemaTests {

    @Test
    void testGenerateRandomSchema() {
        CypherSchema schema = CypherSchema.generateRandomSchema();
        String label = schema.getRandomLabel();
        String type = schema.getRandomType();

        assertNotNull(schema);

        assertNotNull(label);
        assertFalse(label.isEmpty());

        assertNotNull(type);
        assertFalse(type.isEmpty());
    }

    @Test
    void testGenerateIndexName() {
        CypherSchema schema = CypherSchema.generateRandomSchema();
        assertNotEquals(schema.generateRandomNodeIndex(), schema.generateRandomNodeIndex());
    }

}
