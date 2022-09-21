package ch.ethz.ast.gdbmeter.cypher.ast;

import ch.ethz.ast.gdbmeter.util.Randomization;

public class CypherBinaryComparisonOperation
        extends BinaryOperatorNode<CypherExpression, CypherBinaryComparisonOperation.BinaryComparisonOperator>
        implements CypherExpression {

    public enum BinaryComparisonOperator implements Operator {
        EQUALS("="),
        NOT_EQUALS("<>"),
        LESS("<"),
        LESS_EQUALS("<="),
        GREATER(">"),
        GREATER_EQUALS(">=");

        private final String representation;

        BinaryComparisonOperator(String representation) {
            this.representation = representation;
        }

        @Override
        public String getTextRepresentation() {
            return representation;
        }

        public static BinaryComparisonOperator getRandom() {
            return Randomization.fromOptions(values());
        }

    }

    public CypherBinaryComparisonOperation(CypherExpression left, CypherExpression right, BinaryComparisonOperator operator) {
        super(left, right, operator);
    }

}
