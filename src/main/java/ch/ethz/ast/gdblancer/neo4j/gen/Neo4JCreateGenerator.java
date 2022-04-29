package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
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

    public static Neo4JQuery createEntities(Neo4JDBSchema schema) {
        return new Neo4JCreateGenerator(schema).generateCreate();
    }

    private Neo4JQuery generateCreate() {
        Neo4JDBUtil.addRegexErrors(errors);
        Neo4JDBUtil.addArithmeticErrors(errors);
        Neo4JDBUtil.addFunctionErrors(errors);

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

        return new Neo4JQuery(query.toString(), errors);
    }

    private void generateReturn() {
        if (Randomization.getBoolean()) {
            query.append("RETURN * ");
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

        query.append(String.format("(%s)-[%s:%s ", from, name, type));
        query.append(Neo4JPropertyGenerator.generatePropertyQuery(relationshipSchema, getAllVariables()));
        query.append(String.format("]->(%s)", to));

        // Add variable at this point so that we don't reference it before
        relationshipVariables.put(name, relationshipSchema);
    }

    // TODO: Support multiple labels
    private void generateNode() {
        String label = schema.getRandomLabel();
        Neo4JDBEntity nodeSchema = schema.getEntityByLabel(label);
        String name = getUniqueVariableName();

        query.append(String.format("(%s:%s ", name, label));
        query.append(Neo4JPropertyGenerator.generatePropertyQuery(nodeSchema, getAllVariables()));
        query.append(")");

        // Add variable at this point so that we don't reference it before
        nodeVariables.put(name, nodeSchema);
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
