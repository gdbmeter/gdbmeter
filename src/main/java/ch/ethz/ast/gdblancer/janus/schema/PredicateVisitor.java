package ch.ethz.ast.gdblancer.janus.schema;

import com.thoughtworks.qdox.model.expression.PreIncrement;

public interface PredicateVisitor {

    void visit(BinaryPredicate predicate);

    void visit(SimplePredicate predicate);

    default void visit(Predicate predicate) {
        if (predicate instanceof BinaryPredicate) {
            visit((BinaryPredicate) predicate);
        } else if (predicate instanceof  SimplePredicate) {
            visit((SimplePredicate) predicate);
        } else {
            throw new AssertionError(predicate);
        }
    }

    static String asString(Predicate predicate) {
        StringPredicateVisitor visitor = new StringPredicateVisitor();
        visitor.visit(predicate);
        return visitor.get();
    }

}
