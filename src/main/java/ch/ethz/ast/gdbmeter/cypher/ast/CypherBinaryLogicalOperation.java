package ch.ethz.ast.gdbmeter.cypher.ast;

import ch.ethz.ast.gdbmeter.util.Randomization;

public class CypherBinaryLogicalOperation
        extends BinaryOperatorNode<CypherExpression, CypherBinaryLogicalOperation.BinaryLogicalOperator>
        implements CypherExpression {

    public enum BinaryLogicalOperator implements Operator {

        AND,
        OR,
        XOR;

        @Override
        public String getTextRepresentation() {
           return toString();
        }

        public static BinaryLogicalOperator getRandom() {
            return Randomization.fromOptions(values());
        }
    }

    public CypherBinaryLogicalOperation(CypherExpression left, CypherExpression right, BinaryLogicalOperator operator) {
        super(left, right, operator);
    }

}
