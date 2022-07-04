package ch.ethz.ast.gdblancer.common;

import ch.ethz.ast.gdblancer.common.schema.Schema;

public interface OracleFactory<C extends Connection, T> {

    Oracle createOracle(OracleType type, GlobalState<C> state, Schema<T> schema);

}
