package ch.ethz.ast.gdblancer.common.graph;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Node extends GraphElement {

    private final String label;
    private final Set<Edge> edges = new HashSet<>();

    Node(Map<String, Object> properties, String label) {
        super(properties);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    void addEdge(Edge edge) {
        edges.add(edge);
    }

}
