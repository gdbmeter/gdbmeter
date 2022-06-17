package ch.ethz.ast.gdblancer.cypher.ast;

import ch.ethz.ast.gdblancer.util.Randomization;

public class CypherBinaryStringOperation
        extends BinaryOperatorNode<CypherExpression, CypherBinaryStringOperation.BinaryStringOperation>
        implements CypherExpression {

    public enum BinaryStringOperation implements Operator {

        STARTS_WITH("STARTS WITH"),
        ENDS_WITH("ENDS WITH"),
        CONTAINS("CONTAINS");

        private final String representation;

        BinaryStringOperation(String representation) {
            this.representation = representation;
        }


        @Override
        public String getTextRepresentation() {
            return representation;
        }

        public static BinaryStringOperation getRandom() {
            return Randomization.fromOptions(values());
        }

    }

    public CypherBinaryStringOperation(CypherExpression left,
                                       CypherExpression right,
                                       BinaryStringOperation operator) {
        super(left, right, operator);
    }

}
