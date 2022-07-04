package ch.ethz.ast.gdblancer.redis;

import ch.ethz.ast.gdblancer.common.Generator;
import ch.ethz.ast.gdblancer.common.OracleFactory;
import ch.ethz.ast.gdblancer.common.Provider;
import ch.ethz.ast.gdblancer.common.QueryReplay;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.neo4j.schema.Neo4JType;
import ch.ethz.ast.gdblancer.redis.oracle.RedisOracleFactory;
import ch.ethz.ast.gdblancer.redis.schema.RedisType;

import java.util.Set;

public class RedisProvider implements Provider<RedisConnection, RedisType> {

    @Override
    public RedisConnection getConnection() {
        return new RedisConnection();
    }

    @Override
    public Schema<RedisType> getSchema() {
        return Schema.generateRandomSchema(Set.of(RedisType.values()));
    }

    @Override
    public Generator<RedisConnection> getGenerator(Schema schema) {
        return new RedisGenerator(schema);
    }

    @Override
    public OracleFactory<RedisConnection, RedisType> getOracleFactory() {
        return new RedisOracleFactory();
    }

    @Override
    public QueryReplay getQueryReplay() {
        return new RedisQueryReplay();
    }
}
