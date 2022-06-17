package ch.ethz.ast.gdblancer.cypher.ast;

public class CypherToStringVisitor implements CypherVisitor {

    private final StringBuilder sb = new StringBuilder();

    @Override
    public void visit(BinaryOperatorNode<CypherExpression, ?> operation) {
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
        visit(expression.getString());
        sb.append("=~");
        visit(expression.getRegex());
    }

    @Override
    public void visit(CypherFunctionCall functionCall) {
        sb.append(functionCall.getFunctionName());
        sb.append("(");

        String delimiter = "";

        for (CypherExpression argument : functionCall.getArguments()) {
            sb.append(delimiter);
            visit(argument);
            delimiter = ",";
        }

        sb.append(")");
    }

    @Override
    public void visit(CypherVariablePropertyAccess propertyAccess) {
        sb.append(propertyAccess.getVariableName());
    }

    @Override
    public void visit(CypherConstant constant) {
        sb.append(constant.getTextRepresentation());
    }

    @Override
    public void visit(CypherPrefixOperation operation) {
        sb.append(operation.getOperator().getTextRepresentation());
        sb.append("(");
        visit(operation.getExpression());
        sb.append(")");
    }

    @Override
    public void visit(CypherPostfixOperation operation) {
        sb.append("(");
        visit(operation.getExpression());
        sb.append(") ");
        sb.append(operation.getOperator().getTextRepresentation());
    }

    public String get() {
        return sb.toString();
    }

}
