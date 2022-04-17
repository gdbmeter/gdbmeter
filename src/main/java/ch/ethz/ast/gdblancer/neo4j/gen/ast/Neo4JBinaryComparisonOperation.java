package ch.ethz.ast.gdblancer.neo4j.gen.ast;

import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JBinaryComparisonOperation
        extends BinaryOperatorNode<Neo4JExpression, Neo4JBinaryComparisonOperation.BinaryComparisonOperator>
        implements Neo4JExpression {

    public enum BinaryComparisonOperator implements Operator {
        EQUALS("="),
        NOT_EQUALS("<>"),
        LESS("<"),
        LESS_EQUALS("<="),
        GREATER(">"),
        GREATER_EQUALS(">=");

        private final String representation;

        BinaryComparisonOperator(String representation) {
            this.representation = representation;
        }

        @Override
        public String getTextRepresentation() {
            return representation;
        }

        public static BinaryComparisonOperator getRandom() {
            return Randomization.fromOptions(values());
        }

    }

    public Neo4JBinaryComparisonOperation(Neo4JExpression left, Neo4JExpression right, BinaryComparisonOperator operator) {
        super(left, right, operator);
    }

}
