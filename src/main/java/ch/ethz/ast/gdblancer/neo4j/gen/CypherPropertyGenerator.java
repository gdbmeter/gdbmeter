package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.neo4j.gen.ast.Neo4JExpression;
import ch.ethz.ast.gdblancer.neo4j.gen.ast.Neo4JVisitor;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBEntity;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JType;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class CypherPropertyGenerator {

    private final Neo4JDBEntity entity;
    private final Map<String, Neo4JDBEntity> variables;
    private final StringBuilder query = new StringBuilder();

    protected CypherPropertyGenerator(Neo4JDBEntity entity) {
        this.entity = entity;
        this.variables = new HashMap<>();
    }

    protected CypherPropertyGenerator(Neo4JDBEntity entity, Map<String, Neo4JDBEntity> variables) {
        this.entity = entity;
        this.variables = variables;
    }

    public String generateProperties() {
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
            expression = generateConstant(type);
        } else {
            expression = generateExpression(variables, type);
        }

        query.append(Neo4JVisitor.asString(expression));
    }

    protected abstract Neo4JExpression generateConstant(Neo4JType type);
    protected abstract Neo4JExpression generateExpression(Map<String, Neo4JDBEntity> variables, Neo4JType type);

}
