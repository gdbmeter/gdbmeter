package ch.ethz.ast.gdblancer.janus;

import ch.ethz.ast.gdblancer.common.schema.Entity;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Set;

public class JanusCreateGenerator {

    public static JanusQuery createEntities(Schema<JanusType> schema) {
        String label = schema.getRandomLabel();
        Entity<JanusType> entity = schema.getEntityByLabel(label);
        Set<String> selectedProperties = Randomization.nonEmptySubset(entity.getAvailableProperties().keySet());

        StringBuilder query = new StringBuilder(String.format("g.addV('%s')", label));

        for (String property : selectedProperties) {
            JanusType type = entity.getAvailableProperties().get(property);

            Object value;

            switch (type) {
                case STRING:
                    value = escape(Randomization.getString());
                    break;
                case CHARACTER:
                    value = escape("" + Randomization.getCharacter());
                    break;
                case BOOLEAN:
                    value = Randomization.getBoolean();
                    break;
                case BYTE:
                    value = Randomization.getByte();
                    break;
                case SHORT:
                    value = Randomization.getShort();
                    break;
                case INTEGER:
                    value = Randomization.nextInt();
                    break;
                case LONG:
                    value = Randomization.getInteger();
                    break;
                case FLOAT:
                    value = Randomization.nextFloat();
                    break;
                case DOUBLE:
                    value = Randomization.getDouble();
                    break;
                default:
                    throw new AssertionError(type);
            }

            query.append(String.format(".property('%s', '%s')", property, value));
        }

        query.append(".next()");

        return new JanusQuery(query.toString());
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
