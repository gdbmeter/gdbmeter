package ch.ethz.ast.gdbmeter.janus.schema;

import ch.ethz.ast.gdbmeter.util.Randomization;

public record RangePredicate(Type type, Object left,
                             Object right) implements Predicate {

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
