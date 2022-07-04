package ch.ethz.ast.gdblancer.common.schema;

import ch.ethz.ast.gdblancer.cypher.CypherUtil;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Schema<T> {

    private final Map<String, Entity<T>> nodeSchema;
    private final Map<String, Entity<T>> relationshipSchema;
    private Set<String> indices;

    protected Schema(Map<String, Entity<T>> nodeSchema, Map<String, Entity<T>> relationshipSchema) {
        this.nodeSchema = nodeSchema;
        this.relationshipSchema = relationshipSchema;
        this.indices = new HashSet<>();
    }

    public static <E> Schema<E> generateRandomSchema(Set<E> availableTypes) {
        Map<String, Entity<E>> nodeSchema = new HashMap<>();
        Map<String, Entity<E>> relationshipSchema = new HashMap<>();

        for (int i = 0; i < Randomization.nextInt(3, 10); i++) {
            nodeSchema.put(CypherUtil.generateValidName(), Entity.generateRandomEntity(availableTypes));
        }

        for (int i = 0; i < Randomization.nextInt(3, 4); i++) {
            relationshipSchema.put(CypherUtil.generateValidName(), Entity.generateRandomEntity(availableTypes));
        }

        return new Schema<E>(nodeSchema, relationshipSchema);
    }

    public void setIndices(Set<String> indices) {
        this.indices = indices;
    }

    public String getRandomLabel() {
        return Randomization.fromOptions(nodeSchema.keySet().toArray(new String[0]));
    }

    public Entity<T> getEntityByLabel(String label) {
        return nodeSchema.get(label);
    }

    public String getRandomType() {
        return Randomization.fromOptions(relationshipSchema.keySet().toArray(new String[0]));
    }

    public Entity<T> getEntityByType(String type) {
        return relationshipSchema.get(type);
    }

    public String getRandomIndex() {
        return Randomization.fromOptions(indices.toArray(new String[0]));
    }

    public boolean hasIndices() {
        return !indices.isEmpty();
    }

    public Index generateRandomNodeIndex() {
        String label = getRandomLabel();
        Set<String> properties = Randomization.nonEmptySubset(nodeSchema.get(label).getAvailableProperties().keySet());

        return new Index(label, properties);
    }

    public Index generateRandomRelationshipIndex() {
        String type = getRandomType();
        Set<String> properties = Randomization.nonEmptySubset(relationshipSchema.get(type).getAvailableProperties().keySet());

        return new Index(type, properties);
    }

    public Index generateRandomTextIndex(T textType) {
        Map<String, Set<String>> stringProperties = getNodeSchemaByPropertyType(textType);

        if (stringProperties.isEmpty()) {
            throw new IgnoreMeException();
        }

        String label = Randomization.fromSet(stringProperties.keySet());
        String property = Randomization.fromSet(stringProperties.get(label));

        return new Index(label, Set.of(property));
    }

    public String generateRandomIndexName() {
        String name;

        do {
            name = CypherUtil.generateValidName();
        } while (indices.contains(name));

        return name;
    }

    private Map<String, Set<String>> getNodeSchemaByPropertyType(T type) {
        Map<String, Set<String>> schema = new HashMap<>();

        for (String label : nodeSchema.keySet()) {
            Map<String, T> properties = nodeSchema.get(label).getAvailableProperties();

            for (Map.Entry<String, T> entry : properties.entrySet()) {
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
