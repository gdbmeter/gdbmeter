package ch.ethz.ast.gdbmeter.janus.schema;

import ch.ethz.ast.gdbmeter.util.Randomization;

public record BinaryPredicate(Type type,
                              Predicate left,
                              Predicate right) implements Predicate {

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
