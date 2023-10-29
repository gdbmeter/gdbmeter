package ch.ethz.ast.gdbmeter.cypher.gen;

import ch.ethz.ast.gdbmeter.cypher.ast.CypherExpression;
import ch.ethz.ast.gdbmeter.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.util.Randomization;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class CypherPropertyGenerator<T> {

    private final Entity<T> entity;
    private final Map<String, Entity<T>> variables;
    private final StringBuilder query = new StringBuilder();

    protected CypherPropertyGenerator(Entity<T> entity) {
        this.entity = entity;
        this.variables = new HashMap<>();
    }

    protected CypherPropertyGenerator(Entity<T> entity, Map<String, Entity<T>> variables) {
        this.entity = entity;
        this.variables = variables;
    }

    public String generateProperties() {
        Map<String, T> availableProperties = entity.getAvailableProperties();
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

    private void generateProperty(String name, T type) {
        query.append(String.format("%s:", name));

        CypherExpression expression;

        if (Randomization.getBoolean()) {
            expression = generateConstant(type);
        } else {
            expression = generateExpression(variables, type);
        }

        query.append(CypherVisitor.asString(expression));
    }

    protected abstract CypherExpression generateConstant(T type);
    protected abstract CypherExpression generateExpression(Map<String, Entity<T>> variables, T type);

}
