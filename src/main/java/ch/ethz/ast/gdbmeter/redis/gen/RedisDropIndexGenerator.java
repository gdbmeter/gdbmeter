package ch.ethz.ast.gdbmeter.redis.gen;

import ch.ethz.ast.gdbmeter.common.ExpectedErrors;
import ch.ethz.ast.gdbmeter.cypher.gen.CypherIndexTypes;
import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.redis.RedisQuery;
import ch.ethz.ast.gdbmeter.redis.schema.RedisType;
import ch.ethz.ast.gdbmeter.util.Randomization;

/**
 * Redis does not support named indices. Instead, we simply drop a random index and hope that it exists.
 */
public class RedisDropIndexGenerator {

    private final Schema<RedisType> schema;
    private final StringBuilder query = new StringBuilder();
    private final ExpectedErrors errors = new ExpectedErrors();

    public RedisDropIndexGenerator(Schema<RedisType> schema) {
        this.schema = schema;
    }

    public static RedisQuery dropIndex(Schema<RedisType> schema) {
        return new RedisDropIndexGenerator(schema).generateDropIndex();
    }

    private RedisQuery generateDropIndex() {
        switch (Randomization.fromOptions(CypherIndexTypes.values())) {
            case NODE_INDEX:
                dropNodeIndex();
                break;
            case RELATIONSHIP_INDEX:
                dropRelationshipIndex();
                break;
            case TEXT_INDEX:
                dropFulltextIndex();
                break;
            default:
                throw new AssertionError();
        }

        // TODO: Somehow store the label, property combinations to drop indices that definitely exist.
        errors.addRegex("ERR Unable to drop index on (.*) no such index.");

        return new RedisQuery(query.toString(), errors);
    }

    private void dropNodeIndex() {
        String label = schema.getRandomLabel();
        Entity<RedisType> entity = schema.getEntityByLabel(label);
        String property = Randomization.fromSet(entity.getAvailableProperties().keySet());

        query.append(String.format("DROP INDEX ON :%s(%s)", label, property));
    }

    private void dropRelationshipIndex() {
        String type = schema.getRandomType();
        Entity<RedisType> entity = schema.getEntityByType(type);
        String property = Randomization.fromSet(entity.getAvailableProperties().keySet());

        query.append(String.format("DROP INDEX ON :%s(%s)", type, property));
    }

    private void dropFulltextIndex() {
        String label;

        if (Randomization.getBoolean()) {
            label = schema.getRandomLabel();
        } else {
            label = schema.getRandomType();
        }

        query.append(String.format("CALL db.idx.fulltext.drop('%s')", label));
    }

}
