package ch.ethz.ast.gdblancer.cypher.ast;

public interface CypherVisitor {

    void visit(CypherConstant constant);

    void visit(CypherPrefixOperation operation);

    void visit(CypherPostfixOperation operation);

    void visit(BinaryOperatorNode<CypherExpression, ?> operation);

    void visit(CypherRegularExpression expression);

    void visit(CypherFunctionCall functionCall);

    void visit(CypherVariablePropertyAccess propertyAccess);

    default void visit(CypherExpression expression) {
        if (expression instanceof BinaryOperatorNode) {
            visit((BinaryOperatorNode<CypherExpression, ?>) expression);
        } else if (expression instanceof CypherConstant) {
            visit((CypherConstant) expression);
        }  else if (expression instanceof CypherPrefixOperation) {
            visit((CypherPrefixOperation) expression);
        } else if (expression instanceof CypherPostfixOperation) {
            visit((CypherPostfixOperation) expression);
        } else if (expression instanceof CypherRegularExpression) {
            visit((CypherRegularExpression) expression);
        } else if (expression instanceof CypherFunctionCall) {
            visit((CypherFunctionCall) expression);
        } else if (expression instanceof CypherVariablePropertyAccess) {
            visit((CypherVariablePropertyAccess) expression);
        } else {
            throw new AssertionError(expression);
        }
    }

    static String asString(CypherExpression expression) {
        CypherToStringVisitor visitor = new CypherToStringVisitor();
        visitor.visit(expression);
        return visitor.get();
    }

}
