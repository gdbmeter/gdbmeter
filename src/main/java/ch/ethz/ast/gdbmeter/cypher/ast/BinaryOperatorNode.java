package ch.ethz.ast.gdbmeter.cypher.ast;

public class BinaryOperatorNode<T extends CypherExpression, O extends Operator> {

    private final O operator;
    private final T left;
    private final T right;

    public BinaryOperatorNode(T left, T right, O operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public T getLeft() {
        return left;
    }

    public T getRight() {
        return right;
    }

    public O getOperator() {
        return operator;
    }

}
