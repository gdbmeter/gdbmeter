package ch.ethz.ast.gdblancer.cypher.gen;

import ch.ethz.ast.gdblancer.cypher.ast.CypherExpression;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.common.schema.CypherEntity;
import ch.ethz.ast.gdblancer.common.schema.CypherSchema;
import ch.ethz.ast.gdblancer.common.schema.CypherType;
import ch.ethz.ast.gdblancer.redis.RedisBugs;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Map;
import java.util.Set;

public abstract class CypherSetGenerator {

    private final CypherSchema schema;
    protected final StringBuilder query = new StringBuilder();

    public CypherSetGenerator(CypherSchema schema) {
        this.schema = schema;
    }

    // TODO: Support SET on relationships
    // TODO: Support SET on nodes with different labels
    // TODO: Support SET of multiple properties
    protected void generateSet() {
        String label = schema.getRandomLabel();
        CypherEntity entity = schema.getEntityByLabel(label);

        query.append(String.format("MATCH (n:%s)", label));
        query.append(" WHERE ");
        query.append(generateWhereClause(entity));

        if (Randomization.smallBiasProbability()) {
            query.append(" SET n = {}");
        } else {
            Set<String> properties = entity.getAvailableProperties().keySet();
            String property = Randomization.fromSet(properties);
            CypherType type = entity.getAvailableProperties().get(property);
            CypherExpression expression;

            if (Randomization.getBoolean()) {
                expression = generateConstant(type);
            } else {
                expression = generateExpression(type);
            }

            query.append(String.format(" SET n.%s = %s", property, CypherVisitor.asString(expression)));
        }

        if (Randomization.getBoolean() && !RedisBugs.bug2424) {
            query.append(" ");
            query.append(CypherReturnGenerator.returnEntities(Map.of("n", entity)));
        }
    }

    protected abstract String generateWhereClause(CypherEntity entity);
    protected abstract CypherExpression generateConstant(CypherType type);
    protected abstract CypherExpression generateExpression(CypherType type);


}
