package ch.ethz.ast.gdblancer.cypher.ast;

import ch.ethz.ast.gdblancer.util.Randomization;

public class CypherBinaryArithmeticOperation
        extends BinaryOperatorNode<CypherExpression, CypherBinaryArithmeticOperation.ArithmeticOperator>
        implements CypherExpression {

    public enum ArithmeticOperator implements Operator {
        ADDITION("+"),
        SUBTRACTION("-"),
        MULTIPLICATION("*"),
        DIVISION("/"),
        MODULO("%"),
        EXPONENTIATION("^");

        private final String representation;

        ArithmeticOperator(String representation) {
            this.representation = representation;
        }

        @Override
        public String getTextRepresentation() {
            return representation;
        }

        public static ArithmeticOperator getRandomIntegerOperator() {
            return Randomization.fromOptions(ADDITION, SUBTRACTION, MULTIPLICATION, MODULO, DIVISION);
        }

        /**
         * Returns only NaN safe operators i.e. operators that cannot generate NaNs.
         * Strictly speaking multiplication and addition can also generate NaNs when Infinity occurs in the operands.
         * However, this can not happen in our case.
         */
        public static ArithmeticOperator getRandomIntegerOperatorNaNSafe() {
            return Randomization.fromOptions(ADDITION, SUBTRACTION, MULTIPLICATION);
        }

        public static ArithmeticOperator getRandomFloatOperator() {
            return Randomization.fromOptions(values());
        }

        public static ArithmeticOperator getRandomFloatOperatorNaNSafe() {
            return Randomization.fromOptions(ADDITION, SUBTRACTION, MULTIPLICATION);
        }
    }

    public CypherBinaryArithmeticOperation(CypherExpression left,
                                           CypherExpression right,
                                           ArithmeticOperator operator) {
        super(left, right, operator);
    }

}
