package ch.ethz.ast.gdblancer.janus.gen;

import ch.ethz.ast.gdblancer.common.Query;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JanusPropertyUpdateGeneratorTests extends JanusSchemaGenerator {

    @Test
    void testUpdateProperties() {
        Query<?> query = JanusPropertyUpdateGenerator.updateProperties(makeSchema());

        assertNotNull(query);
        assertTrue(query.getQuery().startsWith("g.V().hasLabel('"));
    }

}
