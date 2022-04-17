package ch.ethz.ast.gdblancer.neo4j.gen.ast;

import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JPrefixOperation implements Neo4JExpression {

    public enum PrefixOperator implements Operator {

        NOT;

        @Override
        public String getTextRepresentation() {
            return toString();
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
