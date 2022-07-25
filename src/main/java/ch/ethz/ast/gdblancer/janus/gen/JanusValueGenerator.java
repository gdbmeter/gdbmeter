package ch.ethz.ast.gdblancer.janus.gen;

import ch.ethz.ast.gdblancer.janus.schema.JanusType;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.UUID;

import static ch.ethz.ast.gdblancer.janus.schema.JanusType.LONG;

public class JanusValueGenerator {

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!#<>/.,~-+'*()[]{} ^*?%_\t\r|&\\";

    public static String generate() {
        return generate(Randomization.fromOptions(JanusType.values()));
    }

    public static String generate(JanusType type) {
        switch (type) {
            case STRING:
                return String.format("\"%s\"", escape(Randomization.getStringOfAlphabet(ALPHABET)));
            case CHARACTER:
                return String.format("(char) %s", Randomization.nextInt(0, Character.MAX_VALUE + 1));
            case BOOLEAN:
                return Randomization.getBoolean() ? "true" : "false";
            case BYTE:
                return String.format("(byte) %s", Randomization.getByte());
            case SHORT:
                return String.format("(short) %s", Randomization.getShort());
            case INTEGER:
                return String.format("(int) %s", Randomization.nextInt());
            case LONG:
                return String.format("%sL", Randomization.getInteger());
            case FLOAT:
                return String.format("%sf", Randomization.nextFloat());
            case DOUBLE:
                return String.format("%sd", Randomization.getDouble());
            case UUID:
                return String.format("UUID.fromString('%s')", UUID.randomUUID());
            case DATE:
                return String.format("new Date(%sL)", Randomization.getInteger());
            default:
                throw new AssertionError(type);
        }
    }

    public static String generateWithoutMaxMinValues(JanusType type) {
        if (type == LONG) {
            return String.format("%sL", Randomization.nextLong());
        } else {
            return generate(type);
        }
    }

    private static String escape(String original) {
        StringBuilder sb = new StringBuilder(original.length());

        for (int i = 0; i < original.length(); i++) {
            char c = original.charAt(i);

            switch (c) {
                case '\\':
                case '"':
                case '\'':
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
                        String t = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(t.substring(t.length() - 4));
                    } else {
                        sb.append(c);
                    }
            }
        }

        return sb.toString();
    }


}
