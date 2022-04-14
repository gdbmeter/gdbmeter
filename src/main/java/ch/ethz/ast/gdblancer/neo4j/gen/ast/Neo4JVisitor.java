package ch.ethz.ast.gdblancer.neo4j.gen.ast;

public interface Neo4JVisitor {

    void visit(Neo4JConstant constant);

    default void visit(Neo4JExpression expression) {
        if (expression instanceof Neo4JConstant) {
            visit((Neo4JConstant) expression);
        } else {
            throw new AssertionError(expression);
        }
    }

    static String asString(Neo4JExpression expression) {
        Neo4JToStringVisitor visitor = new Neo4JToStringVisitor();
        visitor.visit(expression);
        return visitor.get();
    }

}
