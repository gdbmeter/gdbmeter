package ch.ethz.ast.gdblancer.janus.schema;

import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Set;

import static ch.ethz.ast.gdblancer.janus.schema.JanusType.*;

// See: https://docs.janusgraph.org/interactions/search-predicates/
public enum JanusPredicate {

    EQUALS("eq"),
    NOT_EQUALS("neq"),
    GREATER_THAN("gt"),
    LESS_THAN("lt"),
    GREATER_THAN_EQUAL("gte"),
    LESS_THAN_EQUAL("lte");

    private final String name;

    JanusPredicate(String name) {
        this.name = name;
    }

    public String toString(String parameter) {
        return name + "(" + parameter + ")";
    }

    public static JanusPredicate compareTo(JanusType type) {
        if (Set.of(STRING,CHARACTER,BYTE,SHORT,INTEGER,LONG,FLOAT,DOUBLE, DATE).contains(type)) {
            return Randomization.fromOptions(JanusPredicate.values());
        } else if (type.equals(UUID) || type.equals(BOOLEAN)) {
            return Randomization.fromOptions(JanusPredicate.EQUALS, JanusPredicate.NOT_EQUALS);
        } else {
            throw new AssertionError(type);
        }
    }

}
