package ch.ethz.ast.gdblancer.redis;

import ch.ethz.ast.gdblancer.common.Generator;
import ch.ethz.ast.gdblancer.common.OracleFactory;
import ch.ethz.ast.gdblancer.common.Provider;
import ch.ethz.ast.gdblancer.common.QueryReplay;
import ch.ethz.ast.gdblancer.common.schema.CypherSchema;
import ch.ethz.ast.gdblancer.redis.oracle.RedisOracleFactory;

public class RedisProvider implements Provider<RedisConnection> {

    @Override
    public RedisConnection getConnection() {
        return new RedisConnection();
    }

    @Override
    public Generator<RedisConnection> getGenerator(CypherSchema schema) {
        return new RedisGenerator(schema);
    }

    @Override
    public OracleFactory<RedisConnection> getOracleFactory() {
        return new RedisOracleFactory();
    }

    @Override
    public QueryReplay getQueryReplay() {
        return new RedisQueryReplay();
    }
}
