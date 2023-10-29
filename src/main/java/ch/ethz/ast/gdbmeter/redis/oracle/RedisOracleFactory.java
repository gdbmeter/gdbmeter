package ch.ethz.ast.gdbmeter.redis.oracle;

import ch.ethz.ast.gdbmeter.common.GlobalState;
import ch.ethz.ast.gdbmeter.common.Oracle;
import ch.ethz.ast.gdbmeter.common.OracleFactory;
import ch.ethz.ast.gdbmeter.common.OracleType;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.redis.RedisConnection;
import ch.ethz.ast.gdbmeter.redis.schema.RedisType;

public class RedisOracleFactory implements OracleFactory<RedisConnection, RedisType> {

    @Override
    public Oracle createOracle(OracleType type, GlobalState<RedisConnection> state, Schema<RedisType> schema) {
        switch (type) {
            case EMPTY_RESULT:
                return new RedisEmptyResultOracle(state, schema);
            case NON_EMPTY_RESULT:
                return new RedisNonEmptyResultOracle(state, schema);
            case PARTITION:
                return new RedisPartitionOracle(state, schema);
            case REFINEMENT:
                return new RedisRefinementOracle(state, schema);
            default:
                throw new AssertionError(type);
        }
    }

}
