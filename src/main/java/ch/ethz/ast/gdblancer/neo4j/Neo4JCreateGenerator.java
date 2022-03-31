package ch.ethz.ast.gdblancer.neo4j;

import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JCreateGenerator {

    private static final String VARIABLE_PREFIX = "v";

    private int variable_id = 0;
    private final StringBuilder query = new StringBuilder();

    public static String createEntities() {
        return new Neo4JCreateGenerator().generateCreate();
    }

    private String generateCreate() {
        query.append("CREATE ");
        generateNode();

        while (Randomization.getBooleanWithRatherLowProbability()) {
            query.append(", ");
            generateNode();
        }

        return query.toString();
    }

    private void generateNode() {
        query.append("(");

        if (Randomization.getBoolean()) {
            query.append(VARIABLE_PREFIX);
            query.append(variable_id++);
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
        query.append("{");

        int iterations = Randomization.smallNumber();
        for (int i = 0; i < iterations; i++) {
            generateProperty(i == iterations - 1);
        }

        query.append("}");
    }

    private void generateProperty(boolean last) {
        query.append(generateValidName());
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
                query.append(Randomization.getString());
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
        query.append(generateValidName());
    }

    /**
     * A valid name begins with an alphabetic character and not with a number
     * Furthermore, a valid name does not contain symbols except for underscores
     */
    private static String generateValidName() {
        return Randomization.getCharacterFromAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")
                + Randomization.getStringOfAlphabet("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_");
    }

}
