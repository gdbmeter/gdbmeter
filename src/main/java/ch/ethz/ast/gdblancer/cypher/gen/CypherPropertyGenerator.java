package ch.ethz.ast.gdblancer.cypher.gen;

import ch.ethz.ast.gdblancer.cypher.ast.CypherExpression;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.common.schema.CypherEntity;
import ch.ethz.ast.gdblancer.common.schema.CypherType;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class CypherPropertyGenerator {

    private final CypherEntity entity;
    private final Map<String, CypherEntity> variables;
    private final StringBuilder query = new StringBuilder();

    protected CypherPropertyGenerator(CypherEntity entity) {
        this.entity = entity;
        this.variables = new HashMap<>();
    }

    protected CypherPropertyGenerator(CypherEntity entity, Map<String, CypherEntity> variables) {
        this.entity = entity;
        this.variables = variables;
    }

    public String generateProperties() {
        Map<String, CypherType> availableProperties = entity.getAvailableProperties();
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

    private void generateProperty(String name, CypherType type) {
        query.append(String.format("%s:", name));

        CypherExpression expression;

        if (Randomization.getBoolean()) {
            expression = generateConstant(type);
        } else {
            expression = generateExpression(variables, type);
        }

        query.append(CypherVisitor.asString(expression));
    }

    protected abstract CypherExpression generateConstant(CypherType type);
    protected abstract CypherExpression generateExpression(Map<String, CypherEntity> variables, CypherType type);

}
