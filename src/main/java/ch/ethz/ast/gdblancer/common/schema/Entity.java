package ch.ethz.ast.gdblancer.common.schema;

import ch.ethz.ast.gdblancer.cypher.CypherUtil;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Describes an entity of a graph database.
 * This could be either a node or an edge.
 */
public class Entity<T> {

    private final Map<String, T> availableProperties;

    protected Entity(Map<String, T> availableProperties) {
        this.availableProperties = availableProperties;
    }

    /**
     * Generates an entity based on a set of available types.
     * The property names are enforced to be unique by consulting takenNames.
     */
    public static <E> Entity<E> generateRandomEntity(Set<E> availableTypes, Set<String> takenNames) {
        Map<String, E> availableProperties = new HashMap<>();

        for (int i = 0; i < Randomization.nextInt(1, 6); i++) {
            String name = Randomization.generateUniqueElement(takenNames, CypherUtil::generateValidName);
            takenNames.add(name);

            availableProperties.put(name, Randomization.fromSet(availableTypes));
        }

        return new Entity<>(availableProperties);
    }

    public Map<String, T> getAvailableProperties() {
        return availableProperties;
    }

}
