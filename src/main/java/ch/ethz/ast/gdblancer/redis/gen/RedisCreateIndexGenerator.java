package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBIndex;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.redis.RedisQuery;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Set;

public class RedisCreateIndexGenerator {

    enum INDEX_TYPES {
        NODE_INDEX,
        RELATIONSHIP_INDEX
        // TEXT_INDEX
    }

    private RedisCreateIndexGenerator(Neo4JDBSchema schema) {
        this.schema = schema;
    }

    private final Neo4JDBSchema schema;
    private final StringBuilder query = new StringBuilder();

    public static RedisQuery createIndex(Neo4JDBSchema schema) {
        return new RedisCreateIndexGenerator(schema).generateCreateIndex();
    }

    private RedisQuery generateCreateIndex() {
        switch (Randomization.fromOptions(INDEX_TYPES.values())) {
            case NODE_INDEX:
                generateNodeIndex();
                break;
            case RELATIONSHIP_INDEX:
                generateRelationshipIndex();
                break;
            default:
                throw new AssertionError();
        }

        return new RedisQuery(query.toString());
    }

    private void generateNodeIndex() {
        Neo4JDBIndex index = schema.generateRandomNodeIndex();
        query.append(String.format("CREATE INDEX FOR (n:%s) ", index.getLabel()));

        generateOnClause(index.getPropertyNames());
    }

    private void generateRelationshipIndex() {
        Neo4JDBIndex index = schema.generateRandomRelationshipIndex();
        query.append(String.format("CREATE INDEX FOR ()-[n:%s]-()", index.getLabel()));

        generateOnClause(index.getPropertyNames());
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
