package ch.ethz.ast.gdblancer.cypher.ast;

import ch.ethz.ast.gdblancer.util.Randomization;

public class CypherPrefixOperation implements CypherExpression {

    public enum PrefixOperator implements Operator {

        NOT("NOT"),
        UNARY_PLUS("+"),
        UNARY_MINUS("-");

        private final String representation;

        PrefixOperator(String representation) {
            this.representation = representation;
        }

        @Override
        public String getTextRepresentation() {
            return representation;
        }

        public static PrefixOperator getRandom() {
            return Randomization.fromOptions(values());
        }

    }

    private final CypherExpression expression;
    private final PrefixOperator operator;

    public CypherPrefixOperation(CypherExpression expression, PrefixOperator operator) {
        this.expression = expression;
        this.operator = operator;
    }

    public CypherExpression getExpression() {
        return expression;
    }

    public PrefixOperator getOperator() {
        return operator;
    }

}
