package ch.ethz.ast.gdbmeter.cypher.gen;

import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.util.Randomization;

import java.util.Map;

public abstract class CypherRemoveGenerator<T> {

    private final Schema<T> schema;
    protected final StringBuilder query = new StringBuilder();

    public CypherRemoveGenerator(Schema<T> schema) {
        this.schema = schema;
    }

    // TODO: Support REMOVE on nodes with different labels
    // TODO: Support REMOVE of multiple properties
    protected void generateRemove() {
        String label = schema.getRandomLabel();
        Entity<T> entity = schema.getEntityByLabel(label);

        query.append(String.format("MATCH (n:%s) WHERE %s", label, generateWhereClause(entity)));

        String property = Randomization.fromSet(entity.getAvailableProperties().keySet());
        query.append(generateRemoveClause(property));

        if (Randomization.getBoolean()) {
            query.append(" ");
            query.append(CypherReturnGenerator.returnEntities(Map.of("n", entity)));
        }
    }

    protected abstract String generateWhereClause(Entity<T> entity);
    protected abstract String generateRemoveClause(String property);

}
