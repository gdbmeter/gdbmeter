package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.cypher.schema.CypherIndex;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.redis.RedisQuery;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Set;

public class RedisCreateIndexGenerator {

    private RedisCreateIndexGenerator(CypherSchema schema) {
        this.schema = schema;
    }

    private final CypherSchema schema;
    private final StringBuilder query = new StringBuilder();

    public static RedisQuery createIndex(CypherSchema schema) {
        return new RedisCreateIndexGenerator(schema).generateCreateIndex();
    }

    private RedisQuery generateCreateIndex() {
        switch (Randomization.fromOptions(RedisIndexTypes.values())) {
            case NODE_INDEX:
                generateNodeIndex();
                break;
            case RELATIONSHIP_INDEX:
                generateRelationshipIndex();
                break;
            case TEXT_INDEX:
                generateFulltextIndex();
                break;
            default:
                throw new AssertionError();
        }

        return new RedisQuery(query.toString());
    }

    private void generateNodeIndex() {
        CypherIndex index = schema.generateRandomNodeIndex();
        query.append(String.format("CREATE INDEX FOR (n:%s) ", index.getLabel()));

        generateOnClause(index.getPropertyNames());
    }

    private void generateRelationshipIndex() {
        CypherIndex index = schema.generateRandomRelationshipIndex();
        query.append(String.format("CREATE INDEX FOR ()-[n:%s]-()", index.getLabel()));

        generateOnClause(index.getPropertyNames());
    }

    // TODO: Support multi-property fulltext search
    private void generateFulltextIndex() {
        CypherIndex index = schema.generateRandomTextIndex();
        query.append(String.format("CALL db.idx.fulltext.createNodeIndex('%s', '%s')", index.getLabel(), index.getPropertyNames().toArray()[0]));
    }

    private void generateOnClause(Set<String> properties) {
        query.append("ON (");
        String delimiter = "";

        for (String property : properties) {
            query.append(delimiter);
            query.append(String.format("n.%s", property));
            delimiter = ", ";
        }

        query.append(")");
    }
}
