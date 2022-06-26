package ch.ethz.ast.gdblancer.common.schema;

import ch.ethz.ast.gdblancer.common.schema.CypherEntity;
import ch.ethz.ast.gdblancer.common.schema.CypherType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CypherEntityTests {

    @Test
    void testGenerateRandomEntity() {
        CypherEntity entity = CypherEntity.generateRandomEntity(CypherType.values());
        Map<String, CypherType> availableProperties = entity.getAvailableProperties();

        assertNotNull(entity);
        assertNotNull(availableProperties);
        assertFalse(availableProperties.isEmpty());
    }

}
