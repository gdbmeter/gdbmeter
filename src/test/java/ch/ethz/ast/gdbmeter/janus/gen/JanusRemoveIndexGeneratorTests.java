package ch.ethz.ast.gdbmeter.janus.gen;

import ch.ethz.ast.gdbmeter.common.Query;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.janus.query.JanusRemoveIndexQuery;
import ch.ethz.ast.gdbmeter.janus.schema.JanusType;
import ch.ethz.ast.gdbmeter.util.IgnoreMeException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class JanusRemoveIndexGeneratorTests extends JanusSchemaGenerator {

    @Test
    void testCreateIndexSuccessful() {
        Schema<JanusType> schema = makeSchema();
        schema.setIndices(Set.of("abc"));

        Query<?> query = JanusRemoveIndexGenerator.dropIndex(schema);

        assertNotNull(query);
        assertInstanceOf(JanusRemoveIndexQuery.class, query);
    }

    @Test
    void testCreateIndexFailure() {
        Schema<JanusType> schema = makeSchema();
        assertThrows(IgnoreMeException.class, () -> JanusRemoveIndexGenerator.dropIndex(schema));
    }

}
