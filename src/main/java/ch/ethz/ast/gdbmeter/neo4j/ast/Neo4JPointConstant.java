package ch.ethz.ast.gdbmeter.neo4j.ast;

import ch.ethz.ast.gdbmeter.cypher.ast.CypherConstant;

public class Neo4JPointConstant extends CypherConstant {

    private final double x;
    private final double y;
    private final Double z;

    public Neo4JPointConstant(double x, double y) {
        this(x, y, null);
    }

    public Neo4JPointConstant(double x, double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String getTextRepresentation() {
        if (z == null) {
            return String.format("point({ x: %f, y: %f })", this.x, this.y);
        } else {
            return String.format("point({ x: %f, y: %f, z: %f })", this.x, this.y, this.z);
        }
    }
}
