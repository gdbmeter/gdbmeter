package ch.ethz.ast.gdblancer.cypher;

import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Set;

public class CypherUtil {

    private static final Set<String> KEYWORDS = Set.of(
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
        } while (KEYWORDS.contains(candidate.toUpperCase()));

        return candidate;
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
