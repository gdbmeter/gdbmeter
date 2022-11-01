package ch.ethz.ast.gdbmeter.janus.gen;

import ch.ethz.ast.gdbmeter.janus.schema.JanusType;
import ch.ethz.ast.gdbmeter.util.Randomization;

import java.util.UUID;

public class JanusValueGenerator {

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!#<>/.,~-+'*()[]{} ^*?%_\t\r|&\\";

    public static String generate() {
        return generate(Randomization.fromOptions(JanusType.values()));
    }

    public static String generate(JanusType type) {
        return switch (type) {
            case STRING -> String.format("\"%s\"", escape(Randomization.getStringOfAlphabet(ALPHABET)));
            case CHARACTER -> String.format("(char) %s", Randomization.nextInt(0, Character.MAX_VALUE + 1));
            case BOOLEAN -> Randomization.getBoolean() ? "true" : "false";
            case BYTE -> String.format("(byte) %s", Randomization.getByte());
            case SHORT -> String.format("(short) %s", Randomization.getShort());
            case INTEGER -> String.format("(int) %s", Randomization.nextInt());
            case LONG -> String.format("%sL", Randomization.getInteger());
            case FLOAT -> String.format("%sf", Randomization.nextFloat());
            case DOUBLE -> String.format("%sd", Randomization.getDouble());
            case UUID -> String.format("UUID.fromString('%s')", UUID.randomUUID());
            case DATE -> String.format("new Date(%sL)", Randomization.getInteger());
        };
    }

    public static String generateWithoutMaxMinValues(JanusType type) {
        return switch (type) {
            case LONG -> String.format("%sL", Randomization.nextLong());
            case DATE -> String.format("new Date(%sL)", Randomization.nextLong());
            default -> generate(type);
        };
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
