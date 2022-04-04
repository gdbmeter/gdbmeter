package ch.ethz.ast.gdblancer.neo4j.gen.schema;

import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Neo4JDBSchema {

    private final Map<String, Neo4JDBEntity> nodeSchema;
    private final Map<String, Neo4JDBEntity> relationshipSchema;
    private final Map<String, Neo4JDBIndex> indices;

    private Neo4JDBSchema(Map<String, Neo4JDBEntity> nodeSchema, Map<String, Neo4JDBEntity> relationshipSchema) {
        this.nodeSchema = nodeSchema;
        this.relationshipSchema = relationshipSchema;
        this.indices = new HashMap<>();
    }

    public static Neo4JDBSchema generateRandomSchema() {
        Map<String, Neo4JDBEntity> nodeSchema = new HashMap<>();
        Map<String, Neo4JDBEntity> relationshipSchema = new HashMap<>();

        for (int i = 0; i < Randomization.nextInt(3, 10); i++) {
            nodeSchema.put(Neo4JDBUtil.generateValidName(), Neo4JDBEntity.generateRandomEntity());
        }

        for (int i = 0; i < Randomization.nextInt(2, 4); i++) {
            relationshipSchema.put(Neo4JDBUtil.generateValidName(), Neo4JDBEntity.generateRandomEntity());
        }

        return new Neo4JDBSchema(nodeSchema, relationshipSchema);
    }

    public String getRandomLabel() {
        return Randomization.fromOptions(nodeSchema.keySet().toArray(new String[0]));
    }

    public Neo4JDBEntity getEntityByLabel(String label) {
        return nodeSchema.get(label);
    }

    public String getRandomType() {
        return Randomization.fromOptions(relationshipSchema.keySet().toArray(new String[0]));
    }

    public Neo4JDBEntity getEntityByType(String type) {
        return relationshipSchema.get(type);
    }

    public String getRandomIndex() {
        return Randomization.fromOptions(indices.keySet().toArray(new String[0]));
    }

    public void removeIndex(String name) {
        indices.remove(name);
    }

    public boolean hasIndices() {
        return !indices.isEmpty();
    }

    public Neo4JDBIndex generateRandomIndex() {
        Neo4JDBIndex index;

        do {
            String label = getRandomLabel();
            String property = getRandomPropertyForLabel(label);

            index = new Neo4JDBIndex(label, Set.of(property));
        } while (indices.containsValue(index));

        return index;
    }

    public String generateRandomIndexName() {
        String name;

        do {
            name = Neo4JDBUtil.generateValidName();
        } while (indices.containsKey(name));

        return name;
    }

    public void registerIndex(String name, Neo4JDBIndex index) {
        indices.put(name, index);
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
