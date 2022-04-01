package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.neo4j.PropertyType;
import ch.ethz.ast.gdblancer.util.Randomization;
import org.apache.commons.text.StringEscapeUtils;

import java.util.EnumSet;

import static java.util.EnumSet.complementOf;

public class Neo4JPropertyGenerator {

    private final boolean allowNullValue;
    private final StringBuilder query = new StringBuilder();

    public Neo4JPropertyGenerator(boolean allowNullValue) {
        this.allowNullValue = allowNullValue;
    }

    public static String generatePropertyQuery(boolean allowNullValue) {
        return new Neo4JPropertyGenerator(allowNullValue).generateProperties();
    }

    private String generateProperties() {
        int iterations = Randomization.smallNumber();

        if (iterations == 0) {
            if (Randomization.getBoolean()) {
                return "";
            }
        }

        query.append("{");

        for (int i = 0; i < iterations; i++) {
            generateProperty(i == iterations - 1);
        }

        query.append("}");
        return query.toString();
    }

    private void generateProperty(boolean last) {
        query.append(Neo4JGraphGenerator.generateValidName());
        query.append(": ");
        generateRandomPropertyValue();

        // TODO: Can we have a trailing comma at the end?
        if (!last) {
            query.append(", ");
        }
    }

    private void generateRandomPropertyValue() {

        PropertyType[] types;

        if (allowNullValue) {
            types = PropertyType.values();
        } else {
            types = complementOf(EnumSet.of(PropertyType.NULL)).toArray(new PropertyType[] {});
        }

        switch (Randomization.fromOptions(types)) {
            case INTEGER:
                query.append(Randomization.getInteger());
                break;
            case FLOAT:
                query.append(Randomization.nextFloat());
                break;
            case STRING:
                query.append("\"");
                query.append(StringEscapeUtils.unescapeJson(Randomization.getString()));
                query.append("\"");
                break;
            case BOOLEAN:
                query.append(Randomization.getBoolean());
                break;
            case NULL:
                query.append("null");
                break;
        }
        ;
    }

}
