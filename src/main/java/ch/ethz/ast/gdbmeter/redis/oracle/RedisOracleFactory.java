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
        return switch (type) {
            case EMPTY_RESULT -> new RedisEmptyResultOracle(state, schema);
            case NON_EMPTY_RESULT -> new RedisNonEmptyResultOracle(state, schema);
            case PARTITION -> new RedisPartitionOracle(state, schema);
            case REFINEMENT -> new RedisRefinementOracle(state, schema);
        };
    }

}
