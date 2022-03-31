package ch.ethz.ast.gdblancer.neo4j;

import ch.ethz.ast.gdblancer.util.Randomization;
import org.apache.commons.text.StringEscapeUtils;
import org.neo4j.cypher.internal.expressions.functions.Rand;

public class Neo4JCreateGenerator {

    private static final String VARIABLE_PREFIX = "v";

    private int variableCounter = 0;
    private final StringBuilder query = new StringBuilder();

    public static String createEntities() {
        return new Neo4JCreateGenerator().generateCreate();
    }

    private String generateCreate() {
        query.append("CREATE ");
        generateNode();

        while (Randomization.getBooleanWithRatherLowProbability()) {

            if (Randomization.getBoolean()) {
                generateRelationship();
            } else {
                query.append(", ");
            }

            generateNode();
        }

        // TODO: Maybe add support for more complex return statements
        if (variableCounter > 0 && Randomization.getBoolean()) {
            query.append(" RETURN ");
            query.append(VARIABLE_PREFIX);
            query.append(Randomization.nextInt(0, variableCounter));
        }

        return query.toString();
    }

    private void generateRelationship() {
        boolean leftToRight = Randomization.getBoolean();

        if (leftToRight) {
            query.append("-");
        } else {
            query.append("<-");
        }

        query.append("[");

        if (Randomization.getBoolean()) {
            query.append(VARIABLE_PREFIX);
            query.append(variableCounter++);
        }

        if (!Randomization.smallBiasProbability()) {
            generateRandomLabel();
        }

        query.append(" ");
        generateProperties();

        query.append("]");

        if (leftToRight) {
            query.append("->");
        } else {
            query.append("-");
        }
    }

    private void generateNode() {
        query.append("(");

        if (Randomization.getBoolean()) {
            query.append(VARIABLE_PREFIX);
            query.append(variableCounter++);
        }

        if (!Randomization.smallBiasProbability()) {
            generateRandomLabel();

            while (Randomization.getBoolean()) {
                generateRandomLabel();
            }

            // TODO: Might not be needed
            query.append(" ");
        }

        generateProperties();

        query.append(")");
    }

    private void generateProperties() {
        int iterations = Randomization.smallNumber();

        if (iterations == 0) {
            if (Randomization.getBoolean()) {
                return;
            }
        }

        query.append("{");

        for (int i = 0; i < iterations; i++) {
            generateProperty(i == iterations - 1);
        }

        query.append("}");
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
        switch (Randomization.fromOptions(PropertyType.values())) {
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
        };
    }

    private void generateRandomLabel() {
        query.append(":");
        query.append(Neo4JGraphGenerator.generateValidName());
    }

}
