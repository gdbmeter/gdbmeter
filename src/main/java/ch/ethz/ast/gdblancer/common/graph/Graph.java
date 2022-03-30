package ch.ethz.ast.gdblancer.common.graph;

import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {

    private final Map<Integer, Node> nodes = new HashMap<>();
    private final Set<Edge> edges = new HashSet<>();

    public Edge addEdge(Map<String, Object> properties, Node leftNode, Node rightNode, String relationshipType) {
        Edge edge = new Edge(properties, leftNode, rightNode, relationshipType);

        leftNode.addEdge(edge);
        rightNode.addEdge(edge);
        edges.add(edge);

        return edge;
    }

    public Node addNode(Map<String, Object> properties, String label, int id) {
        Node node = new Node(properties, label);
        nodes.put(id, node);
        return node;
    }

    public Node getNodeById(int id) {
        return nodes.get(id);
    }

    public Node selectRandomNode() {
        return Randomization.fromOptions(this.nodes.values().toArray(new Node[0]));
    }

    public int getM() {
        return edges.size();
    }

}
