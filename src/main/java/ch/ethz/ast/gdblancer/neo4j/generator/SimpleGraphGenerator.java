package ch.ethz.ast.gdblancer.neo4j.generator;

import ch.ethz.ast.gdblancer.common.graph.Graph;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashMap;
import java.util.Map;

public class SimpleGraphGenerator {

    // It is recommended to use a user-defined property instead of the DB generated one
    private static final String ID_PROPERTY = "id";

    /**
     * Based on: https://networkx.org/documentation/stable/_modules/networkx/generators/random_graphs.html#dense_gnm_random_graph
     * @param n The amount of nodes that the graph should contain
     * @param m The number of edges
     */
    public static Graph generateDenseGraph(int n, int m) {
        int maximumEdges = n * (n - 1) / 2;

        int u = 0;
        int v = 1;
        int t = 0;
        int k = 0;

        Graph graph = new Graph();
        int id = 0;

        for (int i = 0; i < n; i++) {
            Map<String, Object> properties = generateRandomProperties();

            properties.put(ID_PROPERTY, id);
            graph.addNode(properties, generateRandomLabel(), id);
            id++;
        }

        while (true) {
            if (Randomization.nextInt(0, maximumEdges - t) < m - k) {
                graph.addEdge(generateRandomProperties(), graph.getNodeById(u), graph.getNodeById(v), generateRandomRelationshipType());
                k++;

                if (k == m) {
                    return graph;
                }
            }

            t++;
            v++;

            if (v == n) {
                u++;
                v = u + 1;
            }

            if (u >= n || v >= n) {
                System.out.println("not good");
            }
        }
    }

    private static Map<String, Object> generateRandomProperties() {
        Map<String, Object> properties = new HashMap<>();

        for (int i = 0; i < Randomization.smallNumber(); i++) {
            properties.put(generateRandomPropertyName(),generateRandomPropertyValue());
        }

        return properties;
    }

    private static String generateRandomPropertyName() {
        return generateValidName();
    }

    private static Object generateRandomPropertyValue() {
        return switch (Randomization.fromOptions(PropertyType.values())) {
            case INTEGER -> Randomization.getInteger();
            case FLOAT -> Randomization.nextFloat();
            case STRING -> Randomization.getString();
            case BOOLEAN -> Randomization.getBoolean();
        };
    }

    private static String generateRandomLabel() {
        return generateValidName();
    }

    private static String generateRandomRelationshipType() {
        return generateValidName();
    }

    /**
     * A valid name begins with an alphabetic character and not with a number
     * Furthermore, a valid name does not contain symbols except for underscores
     */
    private static String generateValidName() {
        return Randomization.getCharacterFromAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")
                + Randomization.getStringOfAlphabet("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_");
    }

}
