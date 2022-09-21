package ch.ethz.ast.gdbmeter.janus.gen;

import ch.ethz.ast.gdbmeter.common.Query;
import ch.ethz.ast.gdbmeter.janus.query.JanusCreateIndexQuery;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JanusCreateIndexGeneratorTests extends JanusSchemaGenerator {

    @Test
    void testCreateIndex() {
        Query<?> query = JanusCreateIndexGenerator.createIndex(makeSchema());

        assertNotNull(query);
        assertInstanceOf(JanusCreateIndexQuery.class, query);
    }

}
