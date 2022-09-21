package ch.ethz.ast.gdbmeter.janus.gen;

import ch.ethz.ast.gdbmeter.common.Query;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JanusDeleteGeneratorTests extends JanusSchemaGenerator {

    @Test
    void testDeleteNodes() {
        Query<?> query = JanusDeleteGenerator.deleteNodes(makeSchema());

        assertNotNull(query);
        assertTrue(query.getQuery().startsWith("g.V().hasLabel('"));
    }

}
