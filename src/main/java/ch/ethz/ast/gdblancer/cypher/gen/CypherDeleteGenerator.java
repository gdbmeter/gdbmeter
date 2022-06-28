package ch.ethz.ast.gdblancer.cypher.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.schema.CypherEntity;
import ch.ethz.ast.gdblancer.common.schema.CypherSchema;
import ch.ethz.ast.gdblancer.redis.RedisBugs;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Map;

public abstract class CypherDeleteGenerator {

    private final CypherSchema schema;
    protected final StringBuilder query = new StringBuilder();
    protected final ExpectedErrors errors = new ExpectedErrors();

    public CypherDeleteGenerator(CypherSchema schema) {
        this.schema = schema;
    }

    // TODO: Support DELETE of relationships
    // TODO: Support DELETE of nodes of different labels
    protected void generateDelete() {
        String label = schema.getRandomLabel();
        CypherEntity entity = schema.getEntityByLabel(label);

        query.append(String.format("MATCH (n:%s)", label));
        query.append(" WHERE ");
        query.append(generateWhereClause(entity));

        if (Randomization.getBoolean()) {
            query.append(" DETACH");
        } else {
            onNonDetachDelete();
        }

        query.append(" DELETE n");

        if (Randomization.getBoolean() && !RedisBugs.bug2424) {
            query.append(" ");
            query.append(CypherReturnGenerator.returnEntities(Map.of("n", entity)));
        }
    }

    protected abstract String generateWhereClause(CypherEntity entity);
    protected abstract void onNonDetachDelete();

}
