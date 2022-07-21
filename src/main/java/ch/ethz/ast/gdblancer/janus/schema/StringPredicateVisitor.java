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
    public void visit(SimplePredicate predicate) {
        builder.append(predicate.getType().getName());
        builder.append("(");
        builder.append(predicate.getValue());
        builder.append(")");
    }

    public String get() {
        return builder.toString();
    }

}
