package ch.ethz.ast.gdblancer.neo4j.gen.ast;

public class Neo4JConcatOperation
        extends BinaryOperatorNode<Neo4JExpression, Neo4JConcatOperation.ConcatOperator>
        implements Neo4JExpression {

    public interface ConcatOperator extends Operator {}

    public Neo4JConcatOperation(Neo4JExpression left, Neo4JExpression right) {
        super(left, right, () -> "+");
    }

}
