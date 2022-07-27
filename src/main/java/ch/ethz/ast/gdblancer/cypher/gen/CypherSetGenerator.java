package ch.ethz.ast.gdblancer.cypher.gen;

import ch.ethz.ast.gdblancer.common.schema.Entity;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.cypher.ast.CypherExpression;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Map;
import java.util.Set;

public abstract class CypherSetGenerator<T> {

    private final Schema<T> schema;
    protected final StringBuilder query = new StringBuilder();

    public CypherSetGenerator(Schema<T> schema) {
        this.schema = schema;
    }

    // TODO: Support SET on relationships
    // TODO: Support SET on nodes with different labels
    // TODO: Support SET of multiple properties
    protected void generateSet() {
        String label = schema.getRandomLabel();
        Entity<T> entity = schema.getEntityByLabel(label);

        query.append(String.format("MATCH (n:%s)", label));
        query.append(" WHERE ");
        query.append(generateWhereClause(entity));

        if (Randomization.smallBiasProbability()) {
            query.append(" SET n = {}");
        } else {
            Set<String> properties = entity.getAvailableProperties().keySet();
            String property = Randomization.fromSet(properties);
            T type = entity.getAvailableProperties().get(property);
            CypherExpression expression;

            if (Randomization.getBoolean()) {
                expression = generateConstant(type);
            } else {
                expression = generateExpression(type);
            }

            query.append(String.format(" SET n.%s = %s", property, CypherVisitor.asString(expression)));
        }

        if (Randomization.getBoolean()) {
            query.append(" ");
            query.append(CypherReturnGenerator.returnEntities(Map.of("n", entity)));
        }
    }

    protected abstract String generateWhereClause(Entity<T> entity);
    protected abstract CypherExpression generateConstant(T type);
    protected abstract CypherExpression generateExpression(T type);


}
