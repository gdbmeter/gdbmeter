package ch.ethz.ast.gdblancer.neo4j.gen.ast;

import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JPrefixOperation implements Neo4JExpression {

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

    private final Neo4JExpression expression;
    private final PrefixOperator operator;

    public Neo4JPrefixOperation(Neo4JExpression expression, PrefixOperator operator) {
        this.expression = expression;
        this.operator = operator;
    }

    public Neo4JExpression getExpression() {
        return expression;
    }

    public PrefixOperator getOperator() {
        return operator;
    }

}
