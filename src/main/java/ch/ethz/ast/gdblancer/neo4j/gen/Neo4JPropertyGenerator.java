package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.neo4j.gen.ast.Neo4JExpression;
import ch.ethz.ast.gdblancer.neo4j.gen.ast.Neo4JExpressionGenerator;
import ch.ethz.ast.gdblancer.neo4j.gen.ast.Neo4JVisitor;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBEntity;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JType;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Neo4JPropertyGenerator {

    private final Neo4JDBEntity entity;
    private final Map<String, Neo4JDBEntity> variables;
    private final StringBuilder query = new StringBuilder();

    private Neo4JPropertyGenerator(Neo4JDBEntity entity) {
        this.entity = entity;
        this.variables = new HashMap<>();
    }

    private Neo4JPropertyGenerator(Neo4JDBEntity entity, Map<String, Neo4JDBEntity> variables) {
        this.entity = entity;
        this.variables = variables;
    }

    public static String generatePropertyQuery(Neo4JDBEntity entity) {
        return new Neo4JPropertyGenerator(entity).generateProperties();
    }

    public static String generatePropertyQuery(Neo4JDBEntity entity, Map<String, Neo4JDBEntity> variables) {
        return new Neo4JPropertyGenerator(entity, variables).generateProperties();
    }

    private String generateProperties() {
        Map<String, Neo4JType> availableProperties = entity.getAvailableProperties();
        Set<String> selectedProperties = Randomization.nonEmptySubset(availableProperties.keySet());

        if (selectedProperties.isEmpty()) {
            return "";
        }

        query.append("{");
        String delimiter = "";

        for (String property : selectedProperties) {
            query.append(delimiter);
            generateProperty(property, availableProperties.get(property));
            delimiter = ", ";

        }

        query.append("}");
        return query.toString();
    }

    private void generateProperty(String name, Neo4JType type) {
        query.append(String.format("%s:", name));

        Neo4JExpression expression;

        if (Randomization.getBoolean()) {
            expression = Neo4JExpressionGenerator.generateConstant(type);
        } else {
            expression = Neo4JExpressionGenerator.generateExpression(variables, type);
        }

        query.append(Neo4JVisitor.asString(expression));
    }

    // TODO: Remove this method since it shouldn't be necessary anymore?
    public static String generateRandomValue(Neo4JType type) {
        Neo4JExpression expression;

        if (Randomization.getBoolean()) {
            expression = Neo4JExpressionGenerator.generateConstant(type);
        } else {
            expression = Neo4JExpressionGenerator.generateExpression(type);
        }

        return Neo4JVisitor.asString(expression);
    }

}
