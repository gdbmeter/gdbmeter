package ch.ethz.ast.gdbmeter.cypher.ast;

import ch.ethz.ast.gdbmeter.util.Randomization;

public record CypherPostfixOperation(CypherExpression expression,
                                     PostfixOperator operator) implements CypherExpression {

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

}
