package ch.ethz.ast.gdblancer.redis.oracle;

import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Oracle;
import ch.ethz.ast.gdblancer.common.OracleFactory;
import ch.ethz.ast.gdblancer.common.OracleType;
import ch.ethz.ast.gdblancer.common.schema.CypherSchema;
import ch.ethz.ast.gdblancer.redis.RedisConnection;

public class RedisOracleFactory implements OracleFactory<RedisConnection> {

    @Override
    public Oracle createOracle(OracleType type, GlobalState<RedisConnection> state, CypherSchema schema) {
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
