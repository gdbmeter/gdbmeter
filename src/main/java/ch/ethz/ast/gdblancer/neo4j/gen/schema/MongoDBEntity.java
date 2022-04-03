package ch.ethz.ast.gdblancer.neo4j.gen.schema;

import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashMap;
import java.util.Map;

public class MongoDBEntity {

    private final Map<String, MongoDBPropertyType> availableProperties;

    private MongoDBEntity(Map<String, MongoDBPropertyType> availableProperties) {
        this.availableProperties = availableProperties;
    }

    public static MongoDBEntity generateRandomEntity() {
        Map<String, MongoDBPropertyType> availableProperties = new HashMap<>();

        for (int i = 0; i < Randomization.nextInt(1, 6); i++) {
            availableProperties.put(MongoDBUtil.generateValidName(), Randomization.fromOptions(MongoDBPropertyType.values()));
        }

        return new MongoDBEntity(availableProperties);
    }

    public Map<String, MongoDBPropertyType> getAvailableProperties() {
        return availableProperties;
    }

}
