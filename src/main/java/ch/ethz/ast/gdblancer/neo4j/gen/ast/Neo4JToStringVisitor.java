package ch.ethz.ast.gdblancer.neo4j.gen.ast;

public class Neo4JToStringVisitor implements Neo4JVisitor {

    private final StringBuilder sb = new StringBuilder();

    @Override
    public void visit(Neo4JConstant constant) {
        sb.append(constant.getTextRepresentation());
    }

    public String get() {
        return sb.toString();
    }

}
