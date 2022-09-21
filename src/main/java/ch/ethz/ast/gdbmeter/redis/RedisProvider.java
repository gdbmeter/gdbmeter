package ch.ethz.ast.gdbmeter.redis;

import ch.ethz.ast.gdbmeter.common.Generator;
import ch.ethz.ast.gdbmeter.common.OracleFactory;
import ch.ethz.ast.gdbmeter.common.Provider;
import ch.ethz.ast.gdbmeter.common.QueryReplay;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.redis.oracle.RedisOracleFactory;
import ch.ethz.ast.gdbmeter.redis.schema.RedisType;

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
    public Generator<RedisConnection> getGenerator(Schema<RedisType> schema) {
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
