package ch.ethz.ast.gdblancer.janus.gen;

import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.janus.query.JanusRemoveIndexQuery;
import ch.ethz.ast.gdblancer.janus.schema.JanusType;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
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
