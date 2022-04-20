package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.neo4j.gen.ast.Neo4JExpression;
import ch.ethz.ast.gdblancer.neo4j.gen.ast.Neo4JExpressionGenerator;
import ch.ethz.ast.gdblancer.neo4j.gen.ast.Neo4JVisitor;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBEntity;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JType;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Map;
import java.util.Set;

public class Neo4JPropertyGenerator {

    private final Neo4JDBEntity entity;
    private final StringBuilder query = new StringBuilder();

    public Neo4JPropertyGenerator(Neo4JDBEntity entity) {
        this.entity = entity;
    }

    public static String generatePropertyQuery(Neo4JDBEntity entity) {
        return new Neo4JPropertyGenerator(entity).generateProperties();
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
        generateRandomValue(type);
    }

    private void generateRandomValue(Neo4JType type) {
        Neo4JExpression expression;

        if (Randomization.getBoolean()) {
            expression = Neo4JExpressionGenerator.generateConstant(type);
        } else {
            expression = Neo4JExpressionGenerator.generateExpression(0, type);
        }

        query.append(Neo4JVisitor.asString(expression));
    }

}
