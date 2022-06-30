package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.neo4j.schema.Neo4JType;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Neo4JSetGeneratorTests {

    @Test
    void testSetProperties() {
        while (true) {
            try {
                Query<?> query = Neo4JSetGenerator.setProperties(Schema.generateRandomSchema(Set.of(Neo4JType.values())));

                assertNotNull(query);
                assertTrue(query.getQuery().startsWith("MATCH "));
                assertTrue(query.getQuery().contains(" SET n"));
                break;
            } catch (IgnoreMeException ignored) {}
        }
    }

}
