package ch.ethz.ast.gdblancer.neo4j;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Set;

public class Neo4JUtil {

    private static final Set<String> NEO4J_KEYWORDS = Set.of(
            "AND", "CONTAINS", "DISTINCT", "IN", "NOT", "OR", "XOR", "CASE", "WHERE", "FROM", "SHOW", "MATCH", "MERGE", "TRUE", "FALSE"
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
        } while (NEO4J_KEYWORDS.contains(candidate.toUpperCase()));

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
        errors.add("Invalid Regex: capturing group name does not start with a Latin letter");

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
        errors.addRegex("integer, (-??)(\\+??)[0-9]+([.][0-9E]*)?, is too large");
    }

    public static void addFunctionErrors(ExpectedErrors errors) {
        errors.add("Invalid input for length value in function 'left()': Expected a numeric value but got: NO_VALUE");
        errors.add("Invalid input for length value in function 'right()': Expected a numeric value but got: NO_VALUE");
        errors.add("Invalid input for length value in function 'substring()': Expected a numeric value but got: NO_VALUE");
        errors.add("Invalid input for start value in function 'substring()': Expected a numeric value but got: NO_VALUE");
    }

    // Adapted from: https://stackoverflow.com/questions/3020094/how-should-i-escape-strings-in-json
    public static String escape(String original) {
        if (original == null || original.length() == 0) {
            return "\"\"";
        }

        char c;
        int i;
        int len = original.length();
        StringBuilder sb = new StringBuilder(len + 4);
        String t;

        sb.append('"');

        for (i = 0; i < len; i++) {
            c = original.charAt(i);

            switch (c) {
                case '\\':
                case '"':
                case '/':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    if (c < ' ') {
                        t = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(t.substring(t.length() - 4));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append('"');
        return sb.toString();
    }

}
