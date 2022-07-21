package ch.ethz.ast.gdblancer.janus.schema;

import ch.ethz.ast.gdblancer.util.Randomization;

public class SimplePredicate implements Predicate {

    private final Type type;
    private final Object value;

    public SimplePredicate(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public enum Type {
        EQUALS("eq"),
        NOT_EQUALS("neq"),
        GREATER_THAN("gt"),
        LESS_THAN("lt"),
        GREATER_THAN_EQUAL("gte"),
        LESS_THAN_EQUAL("lte");

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
