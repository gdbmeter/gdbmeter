package ch.ethz.ast.gdbmeter.janus.schema;

import ch.ethz.ast.gdbmeter.util.Randomization;

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
        LESS_THAN_EQUAL("lte"),

        // String predicates
        STARTING_WITH("startingWith"),
        ENDING_WITH("endingWith"),
        CONTAINING("containing"),
        NOT_STARTING_WITH("notStartingWith"),
        NOT_ENDING_WITH("notEndingWith"),
        NOT_CONTAINING("notContaining");

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
