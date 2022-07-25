package ch.ethz.ast.gdblancer.janus.schema;

import ch.ethz.ast.gdblancer.util.Randomization;

public class UnaryPredicate implements Predicate {

    private final Type type;
    private final Predicate argument;

    public UnaryPredicate(Type type, Predicate argument) {
        this.type = type;
        this.argument = argument;
    }

    public Type getType() {
        return type;
    }

    public Predicate getArgument() {
        return argument;
    }

    public enum Type {

        AND("not");

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
