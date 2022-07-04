package ch.ethz.ast.gdblancer.common.schema;

import ch.ethz.ast.gdblancer.neo4j.schema.Neo4JType;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EntityTests {

    @Test
    void testGenerateRandomEntity() {
        Entity<Neo4JType> entity = Entity.generateRandomEntity(Set.of(Neo4JType.values()));
        Map<String, Neo4JType> availableProperties = entity.getAvailableProperties();

        assertNotNull(entity);
        assertNotNull(availableProperties);
        assertFalse(availableProperties.isEmpty());
    }

}
