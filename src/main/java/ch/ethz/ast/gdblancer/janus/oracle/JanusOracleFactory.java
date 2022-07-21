package ch.ethz.ast.gdblancer.janus.oracle;

import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Oracle;
import ch.ethz.ast.gdblancer.common.OracleFactory;
import ch.ethz.ast.gdblancer.common.OracleType;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.janus.JanusConnection;
import ch.ethz.ast.gdblancer.janus.schema.JanusType;

public class JanusOracleFactory implements OracleFactory<JanusConnection, JanusType> {

    @Override
    public Oracle createOracle(OracleType type, GlobalState<JanusConnection> state, Schema<JanusType> schema) {
        switch (type) {
            case EMPTY_RESULT:
                return new JanusEmptyResultOracle(state, schema);
            default:
                throw new AssertionError(type);
        }
    }
}
