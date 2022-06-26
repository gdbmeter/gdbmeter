package ch.ethz.ast.gdblancer.common.schema;

import ch.ethz.ast.gdblancer.cypher.CypherUtil;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CypherSchema {

    private final Map<String, CypherEntity> nodeSchema;
    private final Map<String, CypherEntity> relationshipSchema;
    private Set<String> indices;

    private CypherSchema(Map<String, CypherEntity> nodeSchema, Map<String, CypherEntity> relationshipSchema) {
        this.nodeSchema = nodeSchema;
        this.relationshipSchema = relationshipSchema;
        this.indices = new HashSet<>();
    }

    public static CypherSchema generateRandomSchema(CypherType[] availableTypes) {
        Map<String, CypherEntity> nodeSchema = new HashMap<>();
        Map<String, CypherEntity> relationshipSchema = new HashMap<>();

        for (int i = 0; i < Randomization.nextInt(3, 10); i++) {
            nodeSchema.put(CypherUtil.generateValidName(), CypherEntity.generateRandomEntity(availableTypes));
        }

        for (int i = 0; i < Randomization.nextInt(3, 4); i++) {
            relationshipSchema.put(CypherUtil.generateValidName(), CypherEntity.generateRandomEntity(availableTypes));
        }

        return new CypherSchema(nodeSchema, relationshipSchema);
    }

    public static CypherSchema generateRandomSchema() {
        return generateRandomSchema(CypherType.values());
    }

    public void setIndices(Set<String> indices) {
        this.indices = indices;
    }

    public String getRandomLabel() {
        return Randomization.fromOptions(nodeSchema.keySet().toArray(new String[0]));
    }

    public CypherEntity getEntityByLabel(String label) {
        return nodeSchema.get(label);
    }

    public String getRandomType() {
        return Randomization.fromOptions(relationshipSchema.keySet().toArray(new String[0]));
    }

    public CypherEntity getEntityByType(String type) {
        return relationshipSchema.get(type);
    }

    public String getRandomIndex() {
        return Randomization.fromOptions(indices.toArray(new String[0]));
    }

    public boolean hasIndices() {
        return !indices.isEmpty();
    }

    public CypherIndex generateRandomNodeIndex() {
        String label = getRandomLabel();
        Set<String> properties = Randomization.nonEmptySubset(nodeSchema.get(label).getAvailableProperties().keySet());

        return new CypherIndex(label, properties);
    }

    public CypherIndex generateRandomRelationshipIndex() {
        String type = getRandomType();
        Set<String> properties = Randomization.nonEmptySubset(relationshipSchema.get(type).getAvailableProperties().keySet());

        return new CypherIndex(type, properties);
    }

    public CypherIndex generateRandomTextIndex() {
        Map<String, Set<String>> stringProperties = getNodeSchemaByPropertyType(CypherType.STRING);

        if (stringProperties.isEmpty()) {
            throw new IgnoreMeException();
        }

        String label = Randomization.fromSet(stringProperties.keySet());
        String property = Randomization.fromSet(stringProperties.get(label));

        return new CypherIndex(label, Set.of(property));
    }

    public String generateRandomIndexName() {
        String name;

        do {
            name = CypherUtil.generateValidName();
        } while (indices.contains(name));

        return name;
    }

    private Map<String, Set<String>> getNodeSchemaByPropertyType(CypherType type) {
        Map<String, Set<String>> schema = new HashMap<>();

        for (String label : nodeSchema.keySet()) {
            Map<String, CypherType> properties = nodeSchema.get(label).getAvailableProperties();

            for (Map.Entry<String, CypherType> entry : properties.entrySet()) {
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
