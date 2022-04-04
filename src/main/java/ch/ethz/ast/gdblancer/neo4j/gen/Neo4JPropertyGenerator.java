package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBEntity;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBPropertyType;
import ch.ethz.ast.gdblancer.util.Randomization;
import org.apache.commons.text.StringEscapeUtils;

import java.util.Map;

public class Neo4JPropertyGenerator {

    private final Neo4JDBEntity entity;
    private final boolean allowNull;
    private final StringBuilder query = new StringBuilder();

    public Neo4JPropertyGenerator(Neo4JDBEntity entity, boolean allowNull) {
        this.entity = entity;
        this.allowNull = allowNull;
    }

    public static String generatePropertyQuery(Neo4JDBEntity entity, boolean allowNullValue) {
        return new Neo4JPropertyGenerator(entity, allowNullValue).generateProperties();
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
        Map<String, Neo4JDBPropertyType> availableProperties = entity.getAvailableProperties();
        String name = Randomization.fromOptions(availableProperties.keySet().toArray(new String[0]));
        Neo4JDBPropertyType type = availableProperties.get(name);

        query.append(String.format("%s:", name));
        generateRandomValue(type);

        // TODO: Can we have a trailing comma at the end?
        if (!last) {
            query.append(", ");
        }
    }

    private void generateRandomValue(Neo4JDBPropertyType type) {
        if (allowNull && Randomization.getBooleanWithRatherLowProbability()) {
            query.append("null");
            return;
        }

        switch (type) {
            case INTEGER:
                query.append(Randomization.getInteger());
                break;
            case FLOAT:
                query.append(Randomization.nextFloat());
                break;
            case STRING:
                query.append("\"");
                query.append(StringEscapeUtils.escapeJson(Randomization.getString()));
                query.append("\"");
                break;
            case BOOLEAN:
                query.append(Randomization.getBoolean());
                break;
        }
    }

}
