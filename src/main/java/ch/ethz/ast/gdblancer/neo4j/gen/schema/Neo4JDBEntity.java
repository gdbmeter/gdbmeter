package ch.ethz.ast.gdblancer.neo4j.gen.schema;

import ch.ethz.ast.gdblancer.neo4j.gen.util.Neo4JDBUtil;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashMap;
import java.util.Map;

public class Neo4JDBEntity {

    private final Map<String, Neo4JType> availableProperties;

    private Neo4JDBEntity(Map<String, Neo4JType> availableProperties) {
        this.availableProperties = availableProperties;
    }

    public static Neo4JDBEntity generateRandomEntity() {
        Map<String, Neo4JType> availableProperties = new HashMap<>();

        for (int i = 0; i < Randomization.nextInt(1, 6); i++) {
            availableProperties.put(Neo4JDBUtil.generateValidName(), Randomization.fromOptions(Neo4JType.values()));
        }

        return new Neo4JDBEntity(availableProperties);
    }

    public Map<String, Neo4JType> getAvailableProperties() {
        return availableProperties;
    }

}
