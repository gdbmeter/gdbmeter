package ch.ethz.ast.gdblancer.neo4j.gen.ast;

import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JBinaryArithmeticOperation
        extends BinaryOperatorNode<Neo4JExpression, Neo4JBinaryArithmeticOperation.ArithmeticOperator>
        implements Neo4JExpression {

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
            return Randomization.fromOptions(ADDITION, SUBTRACTION, MODULO, DIVISION, MODULO);
        }

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

    public Neo4JBinaryArithmeticOperation(Neo4JExpression left,
                                          Neo4JExpression right,
                                          ArithmeticOperator operator) {
        super(left, right, operator);
    }

}
