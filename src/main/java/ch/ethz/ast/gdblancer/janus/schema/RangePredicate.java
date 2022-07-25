package ch.ethz.ast.gdblancer.janus.schema;

import ch.ethz.ast.gdblancer.util.Randomization;

public class RangePredicate implements Predicate {

    private final Type type;
    private final Object left;
    private final Object right;

    public RangePredicate(Type type, Object left, Object right) {
        this.type = type;
        this.left = left;
        this.right = right;
    }

    public Type getType() {
        return type;
    }

    public Object getLeft() {
        return left;
    }

    public Object getRight() {
        return right;
    }

    public enum Type {
        INSIDE("inside"),
        OUTSIDE("outside"),
        BETWEEN("between");

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
