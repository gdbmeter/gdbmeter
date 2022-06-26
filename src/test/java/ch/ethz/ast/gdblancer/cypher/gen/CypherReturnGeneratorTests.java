package ch.ethz.ast.gdblancer.cypher.gen;

import ch.ethz.ast.gdblancer.common.schema.CypherEntity;
import ch.ethz.ast.gdblancer.common.schema.CypherType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CypherReturnGeneratorTests {

    @Test
    void testReturnEntities() {
        CypherEntity entity = CypherEntity.generateRandomEntity(CypherType.values());
        Map<String, CypherEntity> entities = Map.of("n", entity);
        String query = CypherReturnGenerator.returnEntities(entities);

        assertNotNull(query);
        assertTrue(query.startsWith("RETURN"));
    }

}
