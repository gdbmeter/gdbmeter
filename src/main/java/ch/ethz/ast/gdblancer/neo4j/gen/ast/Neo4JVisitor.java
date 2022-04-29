package ch.ethz.ast.gdblancer.neo4j.gen.ast;

public interface Neo4JVisitor {

    void visit(Neo4JConstant constant);

    void visit(Neo4JPrefixOperation operation);

    void visit(Neo4JPostfixOperation operation);

    void visit(BinaryOperatorNode<Neo4JExpression, ?> operation);

    void visit(Neo4JRegularExpression expression);

    void visit(Neo4JFunctionCall functionCall);

    void visit(Neo4JVariablePropertyAccess propertyAccess);

    default void visit(Neo4JExpression expression) {
        if (expression instanceof BinaryOperatorNode) {
            visit((BinaryOperatorNode<Neo4JExpression, ?>) expression);
        } else if (expression instanceof Neo4JConstant) {
            visit((Neo4JConstant) expression);
        }  else if (expression instanceof Neo4JPrefixOperation) {
            visit((Neo4JPrefixOperation) expression);
        } else if (expression instanceof Neo4JPostfixOperation) {
            visit((Neo4JPostfixOperation) expression);
        } else if (expression instanceof Neo4JRegularExpression) {
            visit((Neo4JRegularExpression) expression);
        } else if (expression instanceof Neo4JFunctionCall) {
            visit((Neo4JFunctionCall) expression);
        } else if (expression instanceof  Neo4JVariablePropertyAccess) {
            visit((Neo4JVariablePropertyAccess) expression);
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
