package ch.ethz.ast.gdbmeter.cypher.gen;

import ch.ethz.ast.gdbmeter.common.ExpectedErrors;
import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.util.Randomization;

import java.util.Map;

public abstract class CypherDeleteGenerator<T> {

    private final Schema<T> schema;
    protected final StringBuilder query = new StringBuilder();
    protected final ExpectedErrors errors = new ExpectedErrors();

    public CypherDeleteGenerator(Schema<T> schema) {
        this.schema = schema;
    }

    // TODO: Support DELETE of relationships
    // TODO: Support DELETE of nodes of different labels
    protected void generateDelete() {
        String label = schema.getRandomLabel();
        Entity<T> entity = schema.getEntityByLabel(label);

        query.append(String.format("MATCH (n:%s)", label));
        query.append(" WHERE ");
        query.append(generateWhereClause(entity));

        if (Randomization.getBoolean()) {
            query.append(" DETACH");
        } else {
            onNonDetachDelete();
        }

        query.append(" DELETE n");

        if (Randomization.getBoolean()) {
            query.append(" ");
            query.append(CypherReturnGenerator.returnEntities(Map.of("n", entity)));
        }
    }

    protected abstract String generateWhereClause(Entity<T> entity);
    protected abstract void onNonDetachDelete();

}
