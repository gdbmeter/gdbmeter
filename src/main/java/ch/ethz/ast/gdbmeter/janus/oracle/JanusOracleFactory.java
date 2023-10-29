package ch.ethz.ast.gdbmeter.janus.oracle;

import ch.ethz.ast.gdbmeter.common.GlobalState;
import ch.ethz.ast.gdbmeter.common.Oracle;
import ch.ethz.ast.gdbmeter.common.OracleFactory;
import ch.ethz.ast.gdbmeter.common.OracleType;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.janus.JanusConnection;
import ch.ethz.ast.gdbmeter.janus.schema.JanusType;

public class JanusOracleFactory implements OracleFactory<JanusConnection, JanusType> {

    @Override
    public Oracle createOracle(OracleType type, GlobalState<JanusConnection> state, Schema<JanusType> schema) {
        switch (type) {
            case EMPTY_RESULT:
                return new JanusEmptyResultOracle(state, schema);
            case NON_EMPTY_RESULT:
                return new JanusNonEmptyResultOracle(state, schema);
            case PARTITION:
                return new JanusPartitionOracle(state, schema);
            default:
                throw new AssertionError(type);
        }
    }
}
