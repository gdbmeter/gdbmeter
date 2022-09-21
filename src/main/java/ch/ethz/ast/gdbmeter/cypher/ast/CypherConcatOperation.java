package ch.ethz.ast.gdbmeter.cypher.ast;

public class CypherConcatOperation
        extends BinaryOperatorNode<CypherExpression, CypherConcatOperation.ConcatOperator>
        implements CypherExpression {

    public interface ConcatOperator extends Operator {}

    public CypherConcatOperation(CypherExpression left, CypherExpression right) {
        super(left, right, () -> "+");
    }

}
