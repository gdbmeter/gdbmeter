package ch.ethz.ast.gdbmeter.janus.schema;

public class StringPredicateVisitor implements PredicateVisitor {

    private final StringBuilder builder = new StringBuilder();

    @Override
    public void visit(BinaryPredicate predicate) {
        visit(predicate.left());
        builder.append(".").append(predicate.type().getName());
        builder.append("(");
        visit(predicate.right());
        builder.append(")");
    }

    @Override
    public void visit(UnaryPredicate predicate) {
        builder.append(predicate.type().getName());
        builder.append("(");
        visit(predicate.argument());
        builder.append(")");
    }

    @Override
    public void visit(SimplePredicate predicate) {
        builder.append(predicate.type().getName());
        builder.append("(");
        builder.append(predicate.value());
        builder.append(")");
    }

    @Override
    public void visit(RangePredicate predicate) {
        builder.append(predicate.type().getName());
        builder.append("(");
        builder.append(predicate.left());
        builder.append(", ");
        builder.append(predicate.right());
        builder.append(")");
    }

    public String get() {
        return builder.toString();
    }

}
