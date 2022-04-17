package ch.ethz.ast.gdblancer.neo4j.gen.ast;

import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JPostfixOperation implements Neo4JExpression {

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

    private final Neo4JExpression expression;
    private final PostfixOperator operator;

    public Neo4JPostfixOperation(Neo4JExpression expression, PostfixOperator operator) {
        this.expression = expression;
        this.operator = operator;
    }

    public Neo4JExpression getExpression() {
        return expression;
    }

    public PostfixOperator getOperator() {
        return operator;
    }
}
