package ch.ethz.ast.gdblancer.janus.gen;

import ch.ethz.ast.gdblancer.common.Query;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JanusCreateGeneratorTests extends JanusSchemaGenerator {

    @Test
    void testCreateEntities() {
        Query<?> query = JanusCreateGenerator.createEntities(makeSchema());

        assertNotNull(query);
        assertTrue(query.getQuery().startsWith("g.addV"));
    }

}
