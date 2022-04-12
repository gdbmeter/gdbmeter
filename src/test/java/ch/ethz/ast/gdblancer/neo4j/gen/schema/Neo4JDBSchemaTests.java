package ch.ethz.ast.gdblancer.neo4j.gen.schema;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// TODO: Improve
public class Neo4JDBSchemaTests {

    @Test
    void testGenerateRandomSchema() {
        Neo4JDBSchema schema = Neo4JDBSchema.generateRandomSchema();
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
        Neo4JDBSchema schema = Neo4JDBSchema.generateRandomSchema();
        assertNotEquals(schema.generateRandomNodeIndex(), schema.generateRandomNodeIndex());
    }

}
