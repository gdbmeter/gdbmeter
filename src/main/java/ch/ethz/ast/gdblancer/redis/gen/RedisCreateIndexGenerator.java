package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.cypher.gen.CypherCreateIndexGenerator;
import ch.ethz.ast.gdblancer.cypher.schema.CypherIndex;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.redis.RedisQuery;

public class RedisCreateIndexGenerator extends CypherCreateIndexGenerator {

    private RedisCreateIndexGenerator(CypherSchema schema) {
        super(schema);
    }

    public static RedisQuery createIndex(CypherSchema schema) {
        RedisCreateIndexGenerator generator = new RedisCreateIndexGenerator(schema);
        generator.generateCreateIndex();

        return new RedisQuery(generator.query.toString());
    }

    @Override
    protected void generateNodeIndex(CypherIndex index) {
        query.append(String.format("CREATE INDEX FOR (n:%s) ", index.getLabel()));
        generateOnClause(index.getPropertyNames());
    }

    @Override
    protected void generateRelationshipIndex(CypherIndex index) {
        query.append(String.format("CREATE INDEX FOR ()-[n:%s]-() ", index.getLabel()));
        generateOnClause(index.getPropertyNames());
    }

    @Override
    protected void generateTextIndex(CypherIndex index) {
        query.append(String.format("CALL db.idx.fulltext.createNodeIndex('%s', '%s')", index.getLabel(), index.getPropertyNames().toArray()[0]));
    }
}
