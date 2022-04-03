package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.neo4j.gen.schema.MongoDBEntity;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.MongoDBSchema;
import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JCreateGenerator {

    private static final String VARIABLE_PREFIX = "v";
    private final MongoDBSchema schema;
    private final StringBuilder query = new StringBuilder();
    private int variableCounter = 0;

    public Neo4JCreateGenerator(MongoDBSchema schema) {
        this.schema = schema;
    }

    public static String createEntities(MongoDBSchema schema) {
        return new Neo4JCreateGenerator(schema).generateInsertion();
    }

    private String generateInsertion() {
        // TODO: Refactor mixed CREATE, MERGE logic
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

        String type = schema.getRandomType();

        query.append(String.format(":%s ", type));
        query.append(Neo4JPropertyGenerator.generatePropertyQuery(schema.getEntityByType(type), allowNullPropertyValues));
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

        // TODO: Support multiple labels
        String label = schema.getRandomLabel();
        MongoDBEntity node = schema.getEntityByLabel(label);
        query.append(String.format(":%s", label));

        query.append(Neo4JPropertyGenerator.generatePropertyQuery(node, allowNullPropertyValues));
        query.append(")");
    }


}
