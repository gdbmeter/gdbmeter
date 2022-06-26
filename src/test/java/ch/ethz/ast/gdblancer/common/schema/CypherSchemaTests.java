package ch.ethz.ast.gdblancer.common.schema;

import ch.ethz.ast.gdblancer.common.schema.CypherEntity;
import ch.ethz.ast.gdblancer.common.schema.CypherSchema;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import org.junit.jupiter.api.Test;

import java.util.Set;

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
    void testGetRandomIndex() {
        CypherSchema schema = CypherSchema.generateRandomSchema();
        String name = "name";
        schema.setIndices(Set.of(name));

        assertEquals(name, schema.getRandomIndex());
    }

    @Test
    void testGenerateRandomNodeIndex() {
        CypherSchema schema = CypherSchema.generateRandomSchema();
        assertNotEquals(schema.generateRandomNodeIndex(), schema.generateRandomNodeIndex());
    }

    @Test
    void testGenerateRandomTextIndex() {
        while (true) {
            CypherSchema schema = CypherSchema.generateRandomSchema();
            try {
                assertNotEquals(schema.generateRandomTextIndex(), schema.generateRandomTextIndex());
                break;
            } catch (IgnoreMeException ignored) {}
        }
    }

    @Test
    void testGenerateRandomRelationshipIndex() {
        CypherSchema schema = CypherSchema.generateRandomSchema();
        assertNotEquals(schema.generateRandomRelationshipIndex(), schema.generateRandomRelationshipIndex());
    }

    @Test
    void testGetEntityByLabel() {
        CypherSchema schema = CypherSchema.generateRandomSchema();
        String label = schema.getRandomLabel();
        CypherEntity entity = schema.getEntityByLabel(label);

        assertNotNull(entity);
    }

    @Test
    void testGetEntityByType() {
        CypherSchema schema = CypherSchema.generateRandomSchema();
        String type = schema.getRandomType();
        CypherEntity entity = schema.getEntityByType(type);

        assertNotNull(entity);
    }

    @Test
    void testTwoRandomIndexNamesAreNotEqual() {
        CypherSchema schema = CypherSchema.generateRandomSchema();
        assertNotEquals(schema.generateRandomIndexName(), schema.generateRandomIndexName());

        String name = "forbidden";
        schema.setIndices(Set.of(name));

        for (int i = 0; i < 100; i++) {
            assertNotEquals(name, schema.generateRandomIndexName());
        }
    }

    @Test
    void testExistingIndexNameIsNotGenerated() {
        CypherSchema schema = CypherSchema.generateRandomSchema();
        String name = "forbidden";
        schema.setIndices(Set.of(name));

        for (int i = 0; i < 100; i++) {
            assertNotEquals(name, schema.generateRandomIndexName());
        }
    }

    @Test
    void testEmptySchemaHasNoIndices() {
        CypherSchema schema = CypherSchema.generateRandomSchema();
        assertFalse(schema.hasIndices());
    }

    @Test
    void testNonEmptySchemaHasIndices() {
        CypherSchema schema = CypherSchema.generateRandomSchema();
        schema.setIndices(Set.of("something"));
        assertTrue(schema.hasIndices());
    }

}
