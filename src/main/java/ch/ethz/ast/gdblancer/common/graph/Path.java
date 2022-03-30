package ch.ethz.ast.gdblancer.common.graph;

import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.List;

public class Path {

    private List<Node> nodes;

    public Path(List<Node> nodes) {
        this.nodes = nodes;
    }

    public int getLength() {
        return nodes.size();
    }

    public Node selectRandomNode() {
        return Randomization.fromOptions(nodes.toArray(new Node[0]));
    }

}
