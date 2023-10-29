package ch.ethz.ast.gdbmeter.cypher.ast;

import ch.ethz.ast.gdbmeter.util.Randomization;

public class CypherPostfixOperation implements CypherExpression {

    public enum PostfixOperator implements Operator {

        IS_NULL("IS NULL"),
        IS_NOT_NULL("IS NOT NULL");

        private final String representation;

        PostfixOperator(String representation) {
            this.representation = representation;
        }

        @Override
        public String getTextRepresentation() {
            return representation;
        }

        public static PostfixOperator getRandom() {
            return Randomization.fromOptions(values());
        }

    }

    private final CypherExpression expression;
    private final PostfixOperator operator;

    public CypherPostfixOperation(CypherExpression expression, PostfixOperator operator) {
        this.expression = expression;
        this.operator = operator;
    }

    public CypherExpression getExpression() {
        return expression;
    }

    public PostfixOperator getOperator() {
        return operator;
    }
}
