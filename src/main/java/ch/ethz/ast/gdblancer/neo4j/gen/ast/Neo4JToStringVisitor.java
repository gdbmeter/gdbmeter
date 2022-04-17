package ch.ethz.ast.gdblancer.neo4j.gen.ast;

public class Neo4JToStringVisitor implements Neo4JVisitor {

    private final StringBuilder sb = new StringBuilder();

    @Override
    public void visit(BinaryOperatorNode<Neo4JExpression, ?> operation) {
        sb.append("(");
        visit(operation.getLeft());
        sb.append(")");

        sb.append(operation.getOperator().getTextRepresentation());

        sb.append("(");
        visit(operation.getRight());
        sb.append(")");
    }

    @Override
    public void visit(Neo4JRegularExpression expression) {
        visit(expression.getString());
        sb.append("=~");
        visit(expression.getRegex());
    }

    @Override
    public void visit(Neo4JConstant constant) {
        sb.append(constant.getTextRepresentation());
    }

    @Override
    public void visit(Neo4JPrefixOperation operation) {
        sb.append(operation.getOperator().getTextRepresentation());
        sb.append(" (");
        visit(operation.getExpression());
        sb.append(")");
    }

    @Override
    public void visit(Neo4JPostfixOperation operation) {
        sb.append("(");
        visit(operation.getExpression());
        sb.append(")" );
        sb.append(operation.getOperator().getTextRepresentation());
    }

    public String get() {
        return sb.toString();
    }

}
