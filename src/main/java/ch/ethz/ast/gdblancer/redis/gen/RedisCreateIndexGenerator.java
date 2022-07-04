package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.cypher.gen.CypherCreateIndexGenerator;
import ch.ethz.ast.gdblancer.common.schema.Index;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.redis.RedisQuery;
import ch.ethz.ast.gdblancer.redis.schema.RedisType;

public class RedisCreateIndexGenerator extends CypherCreateIndexGenerator<RedisType> {

    private RedisCreateIndexGenerator(Schema<RedisType> schema) {
        super(schema, RedisType.STRING);
    }

    public static RedisQuery createIndex(Schema<RedisType> schema) {
        RedisCreateIndexGenerator generator = new RedisCreateIndexGenerator(schema);
        generator.generateCreateIndex();

        return new RedisQuery(generator.query.toString());
    }

    @Override
    protected void generateNodeIndex(Index index) {
        query.append(String.format("CREATE INDEX FOR (n:%s) ", index.getLabel()));
        generateOnClause(index.getPropertyNames());
    }

    @Override
    protected void generateRelationshipIndex(Index index) {
        query.append(String.format("CREATE INDEX FOR ()-[n:%s]-() ", index.getLabel()));
        generateOnClause(index.getPropertyNames());
    }

    @Override
    protected void generateTextIndex(Index index) {
        query.append(String.format("CALL db.idx.fulltext.createNodeIndex('%s', '%s')", index.getLabel(), index.getPropertyNames().toArray()[0]));
    }
}
