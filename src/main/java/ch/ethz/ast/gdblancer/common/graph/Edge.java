package ch.ethz.ast.gdblancer.common.graph;

import java.util.Map;

/**
 * Represents a **directed** edge of the property graph
 */
public class Edge extends GraphElement {

    private final String relationship;
    private final Node source;
    private final Node target;

    Edge(Map<String, Object> properties, Node source, Node target, String relationship) {
        super(properties);
        this.source = source;
        this.target = target;
        this.relationship = relationship;
    }

    public String getRelationship() {
        return relationship;
    }

    public Node getSource() {
        return source;
    }

    public Node getTarget() {
        return target;
    }

}
