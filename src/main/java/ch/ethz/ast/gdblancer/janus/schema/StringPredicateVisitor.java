package ch.ethz.ast.gdblancer.janus.schema;

public class StringPredicateVisitor implements PredicateVisitor {

    private final StringBuilder builder = new StringBuilder();

    @Override
    public void visit(BinaryPredicate predicate) {
        visit(predicate.getLeft());
        builder.append(".").append(predicate.getType().getName());
        builder.append("(");
        visit(predicate.getRight());
        builder.append(")");

    }

    @Override
    public void visit(UnaryPredicate predicate) {
        builder.append(predicate.getType().getName());
        builder.append("(");
        visit(predicate.getArgument());
        builder.append(")");
    }

    @Override
    public void visit(SimplePredicate predicate) {
        builder.append(predicate.getType().getName());
        builder.append("(");
        builder.append(predicate.getValue());
        builder.append(")");
    }

    @Override
    public void visit(RangePredicate predicate) {
        builder.append(predicate.getType().getName());
        builder.append("(");
        builder.append(predicate.getLeft());
        builder.append(", ");
        builder.append(predicate.getRight());
        builder.append(")");
    }

    public String get() {
        return builder.toString();
    }

}
