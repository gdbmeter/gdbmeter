package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBEntity;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.neo4j.gen.util.Neo4JDBUtil;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Neo4JCreateGenerator {

    private final Neo4JDBSchema schema;
    private final StringBuilder query = new StringBuilder();
    private final ExpectedErrors errors = new ExpectedErrors();

    private final Map<String, Neo4JDBEntity> nodeVariables = new HashMap<>();
    private final Map<String, Neo4JDBEntity> relationshipVariables = new HashMap<>();
    private final Set<String> aliasVariables = new HashSet<>();

    private boolean usesDistinctReturn = false;
    private final Set<String> distinctReturnedExpression = new HashSet<>();

    public Neo4JCreateGenerator(Neo4JDBSchema schema) {
        this.schema = schema;
    }

    public static Query createEntities(Neo4JDBSchema schema) {
        return new Neo4JCreateGenerator(schema).generateCreate();
    }

    private Query generateCreate() {
        // TODO: Move to util if needed elsewhere
        errors.add("Invalid Regex: Unclosed character class");
        errors.add("Invalid Regex: Illegal repetition");
        errors.add("Invalid Regex: Unclosed group");
        errors.add("Invalid Regex: Dangling meta character");
        errors.add("Invalid Regex: Illegal/unsupported escape sequence");
        errors.add("Invalid Regex: Unmatched closing");
        errors.add("Invalid Regex: Unclosed counted closure");
        errors.add("Invalid Regex: Illegal character range");
        errors.add("Invalid Regex: Unclosed character family");
        errors.add("Invalid Regex: Unknown inline modifier");
        errors.add("Invalid Regex: \\k is not followed by '<' for named capturing group");

        // See: #12866
        errors.add("Invalid Regex: Unexpected internal error");
        errors.add("Invalid Regex: Unknown character property name");           // RETURN (""=~"5\\P")
        errors.add("Invalid Regex: Illegal octal escape sequence");             // RETURN (""=~"\\0q")
        errors.add("Invalid Regex: Illegal hexadecimal escape sequence");       // RETURN (""=~"\\xp")
        errors.add("Invalid Regex: Illegal character name escape sequence");    // RETURN (""=~"\NZ")
        errors.add("Invalid Regex: Illegal Unicode escape sequence");           // RETURN ""=~("\\uA")

        // Arithmetic errors
        errors.add("/ by zero");
        errors.add("cannot be represented as an integer");
        errors.add("long overflow");
        errors.addRegex("integer, -??[0-9]+([.][0-9]*)?, is too large");

        // Functions
        errors.add("Invalid input for length value in function 'left()': Expected a numeric value but got: NO_VALUE");
        errors.add("Invalid input for length value in function 'right()': Expected a numeric value but got: NO_VALUE");
        errors.add("Invalid input for length value in function 'substring()': Expected a numeric value but got: NO_VALUE");
        errors.add("Invalid input for start value in function 'substring()': Expected a numeric value but got: NO_VALUE");

        // Return clauses
        errors.add("Multiple result columns with the same name are not supported");

        query.append("CREATE ");

        int numberOfNodes = Randomization.nextInt(1, 6);
        String separator = "";

        for (int i = 0; i < numberOfNodes; i++) {
            query.append(separator);
            generateNode();
            separator = ", ";
        }

        for (String from : nodeVariables.keySet()) {
            for (String to : nodeVariables.keySet()) {
                if (Randomization.getBoolean()) {
                    query.append(", ");
                    generateRelationship(from, to);
                }
            }
        }

        if (Randomization.getBoolean()) {
            generateReturn();

            if (Randomization.getBoolean()) {
                generateOrderBy();
            }

            if (Randomization.getBoolean()) {
                generateLimit();
            }
        }

        return new Query(query.toString(), errors);
    }

    private void generateReturn() {
        if (Randomization.getBoolean()) {
            query.append("RETURN *");
            return;
        }

        query.append(" RETURN ");

        if (Randomization.getBoolean()) {
            query.append("DISTINCT ");
            usesDistinctReturn = true;
        }

        Map<String, Neo4JDBEntity> allVariables = getAllVariables();
        Set<String> variables = Randomization.nonEmptySubset(allVariables.keySet());
        String separator = "";

        for (String variable : variables) {
            query.append(separator);
            query.append(variable);

            if (Randomization.getBoolean()) {
                Neo4JDBEntity entity = allVariables.get(variable);
                String property = Randomization.fromSet(entity.getAvailableProperties().keySet());

                query.append(".");
                query.append(property);
                query.append(" ");

                if (usesDistinctReturn) {
                    distinctReturnedExpression.add(variable + "." + property);
                }

                if (Randomization.getBoolean()) {
                    String alias = getUniqueVariableName();
                    query.append(String.format("AS %s ", alias));
                    aliasVariables.add(alias);
                }
            } else {
                query.append(" ");

                if (usesDistinctReturn) {
                    distinctReturnedExpression.add(variable);
                }
            }

            separator = ", ";
        }
    }

    private void generateOrderBy() {
        query.append("ORDER BY ");

        if (usesDistinctReturn) {
            Set<String> expressions = Randomization.nonEmptySubset(distinctReturnedExpression);
            String separator = "";

            for (String expression : expressions) {
                query.append(separator);
                query.append(expression);
                query.append(" ");

                if (Randomization.getBoolean()) {
                    if (Randomization.getBoolean()) {
                        query.append("DESC ");
                    } else {
                        query.append("DESCENDING ");
                    }
                }

                separator = ", ";
            }

            return;
        }

        Map<String, Neo4JDBEntity> allVariables = getAllVariables();
        Set<String> variables = Randomization.nonEmptySubset(allVariables.keySet());
        String separator = "";

        for (String variable : variables) {
            query.append(separator);
            query.append(variable);

            if (Randomization.getBoolean()) {
                Neo4JDBEntity entity = allVariables.get(variable);
                String property = Randomization.fromSet(entity.getAvailableProperties().keySet());

                query.append(".");
                query.append(property);
                query.append(" ");

                if (Randomization.getBoolean()) {
                    if (Randomization.getBoolean()) {
                        query.append("DESC ");
                    } else {
                        query.append("DESCENDING ");
                    }
                }
            }

            separator = ", ";
        }
    }

    private void generateLimit() {
        query.append(" LIMIT ");
        query.append(Randomization.getPositiveInteger());
    }

    private void generateRelationship(String from, String to) {
        String type = schema.getRandomType();
        Neo4JDBEntity relationshipSchema = schema.getEntityByType(type);
        String name = getUniqueVariableName();

        relationshipVariables.put(name, relationshipSchema);

        query.append(String.format("(%s)-[%s:%s ", from, name, type));
        query.append(Neo4JPropertyGenerator.generatePropertyQuery(relationshipSchema));
        query.append(String.format("]->(%s)", to));
    }

    // TODO: Support multiple labels
    private void generateNode() {
        String label = schema.getRandomLabel();
        Neo4JDBEntity nodeSchema = schema.getEntityByLabel(label);
        String name = getUniqueVariableName();

        nodeVariables.put(name, nodeSchema);

        query.append(String.format("(%s:%s ", name, label));
        query.append(Neo4JPropertyGenerator.generatePropertyQuery(nodeSchema));
        query.append(")");
    }

    private String getUniqueVariableName() {
        String name;

        do {
            name = Neo4JDBUtil.generateValidName();
        } while (nodeVariables.containsKey(name)
                || relationshipVariables.containsKey(name)
                || aliasVariables.contains(name));

        return name;
    }

    private Map<String, Neo4JDBEntity> getAllVariables() {
        return Stream.of(nodeVariables, relationshipVariables)
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
