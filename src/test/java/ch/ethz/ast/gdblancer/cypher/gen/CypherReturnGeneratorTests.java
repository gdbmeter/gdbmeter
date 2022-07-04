package ch.ethz.ast.gdblancer.cypher.gen;

import ch.ethz.ast.gdblancer.common.schema.Entity;
import ch.ethz.ast.gdblancer.neo4j.schema.Neo4JType;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CypherReturnGeneratorTests {

    @Test
    void testReturnEntities() {
        Entity<Neo4JType> entity = Entity.generateRandomEntity(Set.of(Neo4JType.values()));
        Map<String, Entity<Neo4JType>> entities = Map.of("n", entity);
        String query = CypherReturnGenerator.returnEntities(entities);

        assertNotNull(query);
        assertTrue(query.startsWith("RETURN"));
    }

}
