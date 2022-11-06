package ch.ethz.ast.gdbmeter.common.schema;

import ch.ethz.ast.gdbmeter.cypher.CypherUtil;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SchemaTests {

    @Test
    void testGenerateRandomSchema() {
        Schema<String> schema = Schema.generateRandomSchema(Set.of("a"));
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
        Schema<String> schema = Schema.generateRandomSchema(Set.of("a"));
        String name = "name";
        schema.setIndices(Set.of(name));

        assertEquals(name, schema.getRandomIndex());
    }

    @Test
    void testGetEntityByLabel() {
        Schema<String> schema = Schema.generateRandomSchema(Set.of("a"));
        String label = schema.getRandomLabel();
        Entity<String> entity = schema.getEntityByLabel(label);

        assertNotNull(entity);
    }

    @Test
    void testGetEntityByType() {
        Schema<String> schema = Schema.generateRandomSchema(Set.of("a"));
        String type = schema.getRandomType();
        Entity<String> entity = schema.getEntityByType(type);

        assertNotNull(entity);
    }

    @Test
    void testTwoRandomIndexNamesAreNotEqual() {
        Schema<String> schema = Schema.generateRandomSchema(Set.of("a"));
        assertNotEquals(schema.generateRandomIndexName(CypherUtil::generateValidName),
                schema.generateRandomIndexName(CypherUtil::generateValidName));

        String name = "forbidden";
        schema.setIndices(Set.of(name));

        for (int i = 0; i < 100; i++) {
            assertNotEquals(name, schema.generateRandomIndexName(CypherUtil::generateValidName));
        }
    }

    @Test
    void testExistingIndexNameIsNotGenerated() {
        Schema<String> schema = Schema.generateRandomSchema(Set.of("a"));
        String name = "forbidden";
        schema.setIndices(Set.of(name));

        for (int i = 0; i < 100; i++) {
            assertNotEquals(name, schema.generateRandomIndexName(CypherUtil::generateValidName));
        }
    }

    @Test
    void testEmptySchemaHasNoIndices() {
        Schema<String> schema = Schema.generateRandomSchema(Set.of("a"));
        assertFalse(schema.hasIndices());
    }

    @Test
    void testNonEmptySchemaHasIndices() {
        Schema<String> schema = Schema.generateRandomSchema(Set.of("a"));
        schema.setIndices(Set.of("something"));
        assertTrue(schema.hasIndices());
    }

    @Test
    void testGetLabelsNotEmpty() {
        Schema<String> schema = Schema.generateRandomSchema(Set.of("a"));
        assertFalse(schema.getLabels().isEmpty());
    }

    @Test
    void testGetTypesNotEmpty() {
        Schema<String> schema = Schema.generateRandomSchema(Set.of("a"));
        assertFalse(schema.getTypes().isEmpty());
    }

}
