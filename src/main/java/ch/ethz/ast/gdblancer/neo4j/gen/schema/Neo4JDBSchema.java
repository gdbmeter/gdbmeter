package ch.ethz.ast.gdblancer.neo4j.gen.schema;

import ch.ethz.ast.gdblancer.neo4j.gen.util.Neo4JDBUtil;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Neo4JDBSchema {

    private final Map<String, Neo4JDBEntity> nodeSchema;
    private final Map<String, Neo4JDBEntity> relationshipSchema;
    private Set<String> indices;

    private Neo4JDBSchema(Map<String, Neo4JDBEntity> nodeSchema, Map<String, Neo4JDBEntity> relationshipSchema) {
        this.nodeSchema = nodeSchema;
        this.relationshipSchema = relationshipSchema;
        this.indices = new HashSet<>();
    }

    public static Neo4JDBSchema generateRandomSchema(Neo4JType[] availableTypes) {
        Map<String, Neo4JDBEntity> nodeSchema = new HashMap<>();
        Map<String, Neo4JDBEntity> relationshipSchema = new HashMap<>();

        for (int i = 0; i < Randomization.nextInt(3, 10); i++) {
            nodeSchema.put(Neo4JDBUtil.generateValidName(), Neo4JDBEntity.generateRandomEntity(availableTypes));
        }

        for (int i = 0; i < Randomization.nextInt(3, 4); i++) {
            relationshipSchema.put(Neo4JDBUtil.generateValidName(), Neo4JDBEntity.generateRandomEntity(availableTypes));
        }

        return new Neo4JDBSchema(nodeSchema, relationshipSchema);
    }

    public static Neo4JDBSchema generateRandomSchema() {
        return generateRandomSchema(Neo4JType.values());
    }

    public void setIndices(Set<String> indices) {
        this.indices = indices;
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
        return Randomization.fromOptions(indices.toArray(new String[0]));
    }

    public boolean hasIndices() {
        return !indices.isEmpty();
    }

    public Neo4JDBIndex generateRandomNodeIndex() {
        String label = getRandomLabel();
        Set<String> properties = Randomization.nonEmptySubset(nodeSchema.get(label).getAvailableProperties().keySet());

        return new Neo4JDBIndex(label, properties);
    }

    public Neo4JDBIndex generateRandomRelationshipIndex() {
        String type = getRandomType();
        Set<String> properties = Randomization.nonEmptySubset(relationshipSchema.get(type).getAvailableProperties().keySet());

        return new Neo4JDBIndex(type, properties);
    }

    public Neo4JDBIndex generateRandomTextIndex() {
        Map<String, Set<String>> stringProperties = getNodeSchemaByPropertyType(Neo4JType.STRING);

        if (stringProperties.isEmpty()) {
            throw new IgnoreMeException();
        }

        String label = Randomization.fromSet(stringProperties.keySet());
        String property = Randomization.fromSet(stringProperties.get(label));

        return new Neo4JDBIndex(label, Set.of(property));
    }

    public String generateRandomIndexName() {
        String name;

        do {
            name = Neo4JDBUtil.generateValidName();
        } while (indices.contains(name));

        return name;
    }

    private Map<String, Set<String>> getNodeSchemaByPropertyType(Neo4JType type) {
        Map<String, Set<String>> schema = new HashMap<>();

        for (String label : nodeSchema.keySet()) {
            Map<String, Neo4JType> properties = nodeSchema.get(label).getAvailableProperties();

            for (Map.Entry<String, Neo4JType> entry : properties.entrySet()) {
                if (entry.getValue() == type) {
                    Set<String> stringProperties = schema.getOrDefault(label, new HashSet<>());
                    stringProperties.add(entry.getKey());
                    schema.put(label, stringProperties);
                }
            }
        }

        return schema;
    }

}
