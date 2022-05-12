package ch.ethz.ast.gdblancer.neo4j.gen.util;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.neo4j.Neo4JBugs;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Set;

public class Neo4JDBUtil {

    private static final Set<String> NEO4J_KEYWORDS = Set.of(
            "AND", "CONTAINS", "DISTINCT", "IN", "NOT", "OR", "XOR", "CASE", "WHERE", "FROM", "SHOW", "MATCH", "MERGE"
    );

    /**
     * A valid name begins with an alphabetic character and not with a number
     * Furthermore, a valid name does not contain symbols except for underscores
     */
    public static String generateValidName() {
        String candidate;

        do {
            candidate = Randomization.getCharacterFromAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")
                    + Randomization.getStringOfAlphabet("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_");
        } while (NEO4J_KEYWORDS.contains(candidate));

        return candidate;
    }

    public static void addRegexErrors(ExpectedErrors errors) {
        errors.add("Invalid Regex: Unclosed character class");
        errors.add("Invalid Regex: Illegal repetition");
        errors.add("Invalid Regex: Unclosed group");
        errors.add("Invalid Regex: Dangling meta character");
        errors.add("Invalid Regex: Illegal/unsupported escape sequence");
        errors.add("Invalid Regex: Unmatched closing");
        errors.add("Invalid Regex: Unclosed counted closure");
        errors.add("Invalid Regex: Illegal character range");
        errors.add("Invalid Regex: Unclosed character family");
        errors.add("Invalid Regex: Unknown inline modifier");
        errors.add("Invalid Regex: \\k is not followed by '<' for named capturing group");
        errors.add("Invalid Regex: Unclosed hexadecimal escape sequence");      // RETURN (""=~"\x{a")
        
        if (Neo4JBugs.bug12866) {
            errors.add("Invalid Regex: Unexpected internal error");
            errors.add("Invalid Regex: Unknown character property name");           // RETURN (""=~"5\\P")
            errors.add("Invalid Regex: Illegal octal escape sequence");             // RETURN (""=~"\\0q")
            errors.add("Invalid Regex: Illegal hexadecimal escape sequence");       // RETURN (""=~"\\xp")
            errors.add("Invalid Regex: Illegal character name escape sequence");    // RETURN (""=~"\NZ")
            errors.add("Invalid Regex: Illegal Unicode escape sequence");           // RETURN ""=~("\\uA")
            errors.add("Invalid Regex: Illegal control escape sequence");           // RETURN ""=~("\\c")
        }
    }

    public static void addArithmeticErrors(ExpectedErrors errors) {
        errors.add("/ by zero");
        errors.add("cannot be represented as an integer");
        errors.add("long overflow");
        errors.addRegex("integer, -??[0-9]+([.][0-9E]*)?, is too large");
    }

    public static void addFunctionErrors(ExpectedErrors errors) {
        errors.add("Invalid input for length value in function 'left()': Expected a numeric value but got: NO_VALUE");
        errors.add("Invalid input for length value in function 'right()': Expected a numeric value but got: NO_VALUE");
        errors.add("Invalid input for length value in function 'substring()': Expected a numeric value but got: NO_VALUE");
        errors.add("Invalid input for start value in function 'substring()': Expected a numeric value but got: NO_VALUE");
    }

}
