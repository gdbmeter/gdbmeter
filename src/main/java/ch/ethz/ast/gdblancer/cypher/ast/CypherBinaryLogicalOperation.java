package ch.ethz.ast.gdblancer.cypher.ast;

import ch.ethz.ast.gdblancer.neo4j.Neo4JBugs;
import ch.ethz.ast.gdblancer.util.Randomization;

public class CypherBinaryLogicalOperation
        extends BinaryOperatorNode<CypherExpression, CypherBinaryLogicalOperation.BinaryLogicalOperator>
        implements CypherExpression {

    public enum BinaryLogicalOperator implements Operator {

        AND,
        OR,
        XOR;

        @Override
        public String getTextRepresentation() {
           return toString();
        }

        public static BinaryLogicalOperator getRandom() {
            if (Neo4JBugs.bug12877) {
                return Randomization.fromOptions(AND, OR);
            } else {
                return Randomization.fromOptions(values());
            }
        }

    }

    public CypherBinaryLogicalOperation(CypherExpression left, CypherExpression right, BinaryLogicalOperator operator) {
        super(left, right, operator);
    }

}
