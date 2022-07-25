package ch.ethz.ast.gdblancer.janus.schema;

import ch.ethz.ast.gdblancer.util.Randomization;

public class BinaryPredicate implements Predicate {

    private final Type type;
    private final Predicate left;
    private final Predicate right;

    public BinaryPredicate(Type type, Predicate left, Predicate right) {
        this.type = type;
        this.left = left;
        this.right = right;
    }

    public Type getType() {
        return type;
    }

    public Predicate getLeft() {
        return left;
    }

    public Predicate getRight() {
        return right;
    }

    public enum Type {

        AND("and"),
        OR("or");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static Type getRandom() {
            return Randomization.fromOptions(values());
        }

    }

}
