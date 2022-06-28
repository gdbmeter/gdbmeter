package ch.ethz.ast.gdblancer.cypher.gen;

import ch.ethz.ast.gdblancer.common.schema.CypherEntity;
import ch.ethz.ast.gdblancer.common.schema.CypherSchema;
import ch.ethz.ast.gdblancer.redis.RedisBugs;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Map;

public abstract class CypherRemoveGenerator {

    private final CypherSchema schema;
    protected final StringBuilder query = new StringBuilder();

    public CypherRemoveGenerator(CypherSchema schema) {
        this.schema = schema;
    }

    // TODO: Support REMOVE on nodes with different labels
    // TODO: Support REMOVE of multiple properties
    protected void generateRemove() {
        String label = schema.getRandomLabel();
        CypherEntity entity = schema.getEntityByLabel(label);

        query.append(String.format("MATCH (n:%s) WHERE %s", label, generateWhereClause(entity)));

        String property = Randomization.fromSet(entity.getAvailableProperties().keySet());
        query.append(generateRemoveClause(property));

        if (Randomization.getBoolean() && !RedisBugs.bug2424) {
            query.append(" ");
            query.append(CypherReturnGenerator.returnEntities(Map.of("n", entity)));
        }
    }

    protected abstract String generateWhereClause(CypherEntity entity);
    protected abstract String generateRemoveClause(String property);

}
