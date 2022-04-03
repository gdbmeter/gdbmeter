package ch.ethz.ast.gdblancer.neo4j.gen.schema;

import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.*;

public class MongoDBSchema {

    private final Map<String, MongoDBEntity> nodeSchema;
    private final Map<String, MongoDBEntity> relationshipSchema;
    private final Set<String> indexNames = new HashSet<>();

    private MongoDBSchema(Map<String, MongoDBEntity> nodeSchema, Map<String, MongoDBEntity> relationshipSchema) {
        this.nodeSchema = nodeSchema;
        this.relationshipSchema = relationshipSchema;
    }

    public static MongoDBSchema generateRandomSchema() {
        Map<String, MongoDBEntity> nodeSchema = new HashMap<>();
        Map<String, MongoDBEntity> relationshipSchema = new HashMap<>();

        for (int i = 0; i < Randomization.nextInt(3, 10); i++) {
            nodeSchema.put(MongoDBUtil.generateValidName(), MongoDBEntity.generateRandomEntity());
        }

        for (int i = 0; i < Randomization.nextInt(2, 4); i++) {
            relationshipSchema.put(MongoDBUtil.generateValidName(), MongoDBEntity.generateRandomEntity());
        }

        return new MongoDBSchema(nodeSchema, relationshipSchema);
    }

    public String getRandomLabel() {
        return Randomization.fromOptions(nodeSchema.keySet().toArray(new String[0]));
    }

    public MongoDBEntity getEntityByLabel(String label) {
        return nodeSchema.get(label);
    }

    public String getRandomType() {
        return Randomization.fromOptions(relationshipSchema.keySet().toArray(new String[0]));
    }

    public MongoDBEntity getEntityByType(String type) {
        return relationshipSchema.get(type);
    }

    public String generateIndexName() {
        String candidate;

        do {
            candidate = MongoDBUtil.generateValidName();
        } while (indexNames.contains(candidate));

        indexNames.add(candidate);
        return candidate;
    }

    public String getRandomPropertyForLabel(String label) {
        Set<String> properties = nodeSchema.get(label).getAvailableProperties().keySet();
        return Randomization.fromOptions(properties.toArray(new String[0]));
    }

    public String getRandomPropertyForRelationship(String type) {
        Set<String> properties = relationshipSchema.get(type).getAvailableProperties().keySet();
        return Randomization.fromOptions(properties.toArray(new String[0]));
    }

}
