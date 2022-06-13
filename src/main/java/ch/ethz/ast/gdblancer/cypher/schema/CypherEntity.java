package ch.ethz.ast.gdblancer.cypher.schema;

import ch.ethz.ast.gdblancer.cypher.CypherUtil;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashMap;
import java.util.Map;

public class CypherEntity {

    private final Map<String, CypherType> availableProperties;

    private CypherEntity(Map<String, CypherType> availableProperties) {
        this.availableProperties = availableProperties;
    }

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
