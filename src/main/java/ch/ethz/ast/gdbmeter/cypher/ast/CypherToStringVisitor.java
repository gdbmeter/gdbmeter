package ch.ethz.ast.gdbmeter.cypher.ast;

public class CypherToStringVisitor implements CypherVisitor {

    private final StringBuilder sb = new StringBuilder();

    @Override
    public void visit(BinaryOperatorNode<? extends CypherExpression, ?> operation) {
        sb.append("(");
        visit(operation.getLeft());
        sb.append(")");

        sb.append(operation.getOperator().getTextRepresentation());

        sb.append("(");
        visit(operation.getRight());
        sb.append(")");
    }

    @Override
    public void visit(CypherRegularExpression expression) {
        visit(expression.string());
        sb.append("=~");
        visit(expression.regex());
    }

    @Override
    public void visit(CypherFunctionCall<?> functionCall) {
        sb.append(functionCall.functionName());
        sb.append("(");

        String delimiter = "";

        for (CypherExpression argument : functionCall.arguments()) {
            sb.append(delimiter);
            visit(argument);
            delimiter = ",";
        }

        sb.append(")");
    }

    @Override
    public void visit(CypherVariablePropertyAccess propertyAccess) {
        sb.append(propertyAccess.variableName());
    }

    @Override
    public void visit(CypherConstant constant) {
        sb.append(constant.getTextRepresentation());
    }

    @Override
    public void visit(CypherPrefixOperation operation) {
        sb.append(operation.operator().getTextRepresentation());
        sb.append("(");
        visit(operation.expression());
        sb.append(")");
    }

    @Override
    public void visit(CypherPostfixOperation operation) {
        sb.append("(");
        visit(operation.expression());
        sb.append(") ");
        sb.append(operation.operator().getTextRepresentation());
    }

    public String get() {
        return sb.toString();
    }

}
