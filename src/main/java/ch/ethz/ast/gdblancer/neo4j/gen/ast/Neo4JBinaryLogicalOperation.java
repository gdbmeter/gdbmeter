package ch.ethz.ast.gdblancer.neo4j.gen.ast;

import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JBinaryLogicalOperation
        extends BinaryOperatorNode<Neo4JExpression, Neo4JBinaryLogicalOperation.BinaryLogicalOperator>
        implements Neo4JExpression {

    public enum BinaryLogicalOperator implements Operator {

        AND,
        OR,
        XOR;

        @Override
        public String getTextRepresentation() {
           return toString();
        }

        public static BinaryLogicalOperator getRandom() {
            return Randomization.fromOptions(values());
        }

    }

    public Neo4JBinaryLogicalOperation(Neo4JExpression left, Neo4JExpression right, BinaryLogicalOperator operator) {
        super(left, right, operator);
    }

}
