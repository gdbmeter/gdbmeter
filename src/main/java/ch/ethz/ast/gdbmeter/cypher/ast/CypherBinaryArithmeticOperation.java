package ch.ethz.ast.gdbmeter.cypher.ast;

import ch.ethz.ast.gdbmeter.util.Randomization;

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

        public static ArithmeticOperator getRandomFloatOperator() {
            return Randomization.fromOptions(values());
        }

    }

    public CypherBinaryArithmeticOperation(CypherExpression left,
                                           CypherExpression right,
                                           ArithmeticOperator operator) {
        super(left, right, operator);
    }

}
