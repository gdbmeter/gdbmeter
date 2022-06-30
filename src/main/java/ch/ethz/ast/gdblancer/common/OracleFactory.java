package ch.ethz.ast.gdblancer.common;

import ch.ethz.ast.gdblancer.common.schema.CypherSchema;

public interface OracleFactory<C extends Connection> {

    Oracle createOracle(OracleType type, GlobalState<C> state, CypherSchema schema);

}
