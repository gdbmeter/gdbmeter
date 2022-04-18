package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBEntity;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.neo4j.gen.util.Neo4JDBUtil;
import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JCreateGenerator {

    private static final String VARIABLE_PREFIX = "v";
    private final Neo4JDBSchema schema;
    private final StringBuilder query = new StringBuilder();
    private int variableCounter = 0;
    private final ExpectedErrors errors = new ExpectedErrors();

    public Neo4JCreateGenerator(Neo4JDBSchema schema) {
        this.schema = schema;
    }

    public static Query createEntities(Neo4JDBSchema schema) {
        return new Neo4JCreateGenerator(schema).generateInsertion();
    }

    private Query generateInsertion() {
        // TODO: Move to util if needed elsewhere
        errors.add("Invalid Regex: Unclosed character class");
        errors.add("Invalid Regex: Illegal repetition");
        errors.add("Invalid Regex: Unclosed group");
        errors.add("Invalid Regex: Dangling meta character");
        errors.add("Invalid Regex: Illegal/unsupported escape sequence");
        errors.add("Invalid Regex: Unmatched closing");
        errors.add("Invalid Regex: Unclosed counted closure");

        // See: #12866
        errors.add("Invalid Regex: Unexpected internal error");
        errors.add("Invalid Regex: Unknown character property name");       // WITH (""=~"5\\P") AS x RETURN x
        errors.add("Invalid Regex: Illegal octal escape sequence");         // WITH (""=~"\\0q") AS x RETURN x
        errors.add("Invalid Regex: Illegal hexadecimal escape sequence");   // WITH (""=~"\\xp") AS x RETURN x

        errors.add("/ by zero");

        query.append("CREATE ");
        generateNode();

        while (Randomization.getBooleanWithRatherLowProbability()) {
            if (Randomization.getBoolean()) {
                generateRelationship();
                generateNode();
            } else {
                query.append(", ");
                generateNode();
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

                if (Randomization.getBoolean()) {
                    query.append(" AS ");
                    query.append(Neo4JDBUtil.generateValidName());
                }
            } else {
                query.append("*");
            }

            if (Randomization.getBoolean()) {
                query.append(" LIMIT ");
                query.append(Randomization.getPositiveInteger());
            }
        }

        return new Query(query.toString(), errors);
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

        String type = schema.getRandomType();

        query.append(String.format(":%s ", type));
        query.append(Neo4JPropertyGenerator.generatePropertyQuery(schema.getEntityByType(type)));
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

        // TODO: Support multiple labels
        String label = schema.getRandomLabel();
        Neo4JDBEntity node = schema.getEntityByLabel(label);
        query.append(String.format(":%s", label));

        query.append(Neo4JPropertyGenerator.generatePropertyQuery(node));
        query.append(")");
    }

}
