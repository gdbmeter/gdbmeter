package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JCreateGenerator {

    private static final String VARIABLE_PREFIX = "v";

    private int variableCounter = 0;
    private final StringBuilder query = new StringBuilder();

    public static String createEntities() {
        return new Neo4JCreateGenerator().generateInsertion();
    }

    private String generateInsertion() {
        boolean previousPartIsCreate = false;

        if (Randomization.getBoolean()) {
            previousPartIsCreate = true;
            query.append("CREATE ");
            generateNode(true);
        } else {
            query.append("MERGE ");
            generateNode(false);
        }

        while (Randomization.getBooleanWithRatherLowProbability()) {
            if (Randomization.getBoolean()) {
                generateRelationship(previousPartIsCreate);
                generateNode(previousPartIsCreate);
            } else {
                if (previousPartIsCreate) {
                    if (Randomization.getBoolean()) {
                        query.append(", ");
                        generateNode(true);
                    } else {
                        previousPartIsCreate = false;
                        query.append(" MERGE ");
                        generateNode(false);
                    }
                } else {
                    if (Randomization.getBoolean()) {
                        previousPartIsCreate = true;
                        query.append(" CREATE ");
                        generateNode(true);
                    } else {
                        query.append(" MERGE ");
                        generateNode(false);
                    }
                }
            }
        }

        // TODO: Maybe add support for more complex return statements
        if (variableCounter > 0 && Randomization.getBoolean()) {
            query.append(" RETURN ");

            if (Randomization.getBoolean()) {
                query.append("DISTINCT ");
            }

            if (Randomization.getBoolean()) {
                query.append(VARIABLE_PREFIX);
                query.append(Randomization.nextInt(0, variableCounter));
            } else {
                query.append("*");
            }

            if (Randomization.getBoolean()) {
                query.append(" LIMIT ");
                query.append(Randomization.getPositiveInteger());
            }
        }

        return query.toString();
    }

    private void generateRelationship(boolean allowNullPropertyValues) {
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

        query.append(Neo4JLabelGenerator.generateRandomLabel());

        query.append(" ");
        query.append(Neo4JPropertyGenerator.generatePropertyQuery(allowNullPropertyValues));
        query.append("]");

        if (leftToRight) {
            query.append("->");
        } else {
            query.append("-");
        }
    }

    private void generateNode(boolean allowNullPropertyValues) {
        query.append("(");

        if (Randomization.getBoolean()) {
            query.append(VARIABLE_PREFIX);
            query.append(variableCounter++);
        }

        if (!Randomization.smallBiasProbability()) {
            query.append(Neo4JLabelGenerator.generateRandomLabel());

            while (Randomization.getBoolean()) {
                query.append(Neo4JLabelGenerator.generateRandomLabel());
            }

            // TODO: Might not be needed
            query.append(" ");
        }

        query.append(Neo4JPropertyGenerator.generatePropertyQuery(allowNullPropertyValues));

        query.append(")");
    }


}
