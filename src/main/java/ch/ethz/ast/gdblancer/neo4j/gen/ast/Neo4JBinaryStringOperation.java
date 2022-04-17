package ch.ethz.ast.gdblancer.neo4j.gen.ast;

import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JBinaryStringOperation
        extends BinaryOperatorNode<Neo4JExpression, Neo4JBinaryStringOperation.BinaryStringOperation>
        implements Neo4JExpression {

    public enum BinaryStringOperation implements Operator {

        STARTS_WITH("STARTS WITH"),
        ENDS_WITH("ENDS WITH"),
        CONTAINS("CONTAINS");

        private final String representation;

        BinaryStringOperation(String representation) {
            this.representation = representation;
        }


        @Override
        public String getTextRepresentation() {
            return representation;
        }

        public static BinaryStringOperation getRandom() {
            return Randomization.fromOptions(values());
        }

    }

    public Neo4JBinaryStringOperation(Neo4JExpression left,
                                      Neo4JExpression right,
                                      BinaryStringOperation operator) {
        super(left, right, operator);
    }

}
