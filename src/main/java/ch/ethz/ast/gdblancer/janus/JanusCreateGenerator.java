package ch.ethz.ast.gdblancer.janus;

import ch.ethz.ast.gdblancer.common.schema.Entity;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Set;
import java.util.UUID;

public class JanusCreateGenerator {

    private final Schema<JanusType> schema;
    private final StringBuilder query = new StringBuilder();

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!#<>/.,~-+'*()[]{} ^*?%_\t\r|&\\";

    public JanusCreateGenerator(Schema<JanusType> schema) {
        this.schema = schema;
    }

    public static JanusQuery createEntities(Schema<JanusType> schema) {
        return new JanusCreateGenerator(schema).generateAddV();
    }

    private JanusQuery generateAddV() {
        query.append("g");

        for (int i = 0; i < Randomization.nextInt(1, 6); i++) {
            generateNode();
        }

        return new JanusQuery(query.toString());
    }

    private void generateNode() {
        String label = schema.getRandomLabel();
        Entity<JanusType> entity = schema.getEntityByLabel(label);
        Set<String> selectedProperties = Randomization.nonEmptySubset(entity.getAvailableProperties().keySet());

        query.append(String.format(".addV('%s')", label));

        for (String property : selectedProperties) {
            JanusType type = entity.getAvailableProperties().get(property);

            query.append(String.format(".property('%s', ", property));

            switch (type) {
                case STRING:
                    query.append(String.format("\"%s\"", escape(Randomization.getStringOfAlphabet(ALPHABET))));
                    break;
                case CHARACTER:
                    query.append(String.format("(char) %s", Randomization.nextInt(0, Character.MAX_VALUE + 1)));
                    break;
                case BOOLEAN:
                    query.append(Randomization.getBoolean() ? "true" : "false");
                    break;
                case BYTE:
                    query.append(String.format("(byte) %s", Randomization.getByte()));
                    break;
                case SHORT:
                    query.append(String.format("(short) %s", Randomization.getShort()));
                    break;
                case INTEGER:
                    query.append(String.format("(int) %s", Randomization.nextInt()));
                    break;
                case LONG:
                    query.append(String.format("%sL", Randomization.getInteger()));
                    break;
                case FLOAT:
                    query.append(String.format("%sf", Randomization.nextFloat()));
                    break;
                case DOUBLE:
                    query.append(String.format("%sd", Randomization.getDouble()));
                    break;
                case UUID:
                    query.append(String.format("UUID.fromString('%s')", UUID.randomUUID()));
                    break;
                default:
                    throw new AssertionError(type);
            }

            query.append(")");
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
