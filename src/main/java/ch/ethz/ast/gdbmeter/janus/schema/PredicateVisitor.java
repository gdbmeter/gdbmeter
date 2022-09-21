package ch.ethz.ast.gdbmeter.janus.schema;

public interface PredicateVisitor {

    void visit(BinaryPredicate predicate);

    void visit(UnaryPredicate predicate);

    void visit(SimplePredicate predicate);

    void visit(RangePredicate predicate);

    default void visit(Predicate predicate) {
        if (predicate instanceof BinaryPredicate) {
            visit((BinaryPredicate) predicate);
        } else if (predicate instanceof UnaryPredicate) {
            visit((UnaryPredicate) predicate);
        } else if (predicate instanceof SimplePredicate) {
            visit((SimplePredicate) predicate);
        } else if (predicate instanceof RangePredicate) {
            visit((RangePredicate) predicate);
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
