package ch.ethz.ast.gdblancer.cypher.schema;

import ch.ethz.ast.gdblancer.cypher.CypherUtil;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashMap;
import java.util.Map;

/**
 * Describes an entity of a cypher database.
 * This could be either a node or an edge.
 */
public class CypherEntity {

    private final Map<String, CypherType> availableProperties;

    private CypherEntity(Map<String, CypherType> availableProperties) {
        this.availableProperties = availableProperties;
    }

    /**
     * Generates a cypher entity based on a set of available types.
     */
    public static CypherEntity generateRandomEntity(CypherType[] availableTypes) {
        Map<String, CypherType> availableProperties = new HashMap<>();

        for (int i = 0; i < Randomization.nextInt(1, 6); i++) {
            availableProperties.put(CypherUtil.generateValidName(), Randomization.fromOptions(availableTypes));
        }

        return new CypherEntity(availableProperties);
    }

    public Map<String, CypherType> getAvailableProperties() {
        return availableProperties;
    }

}
