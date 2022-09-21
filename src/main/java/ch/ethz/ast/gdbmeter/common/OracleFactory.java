package ch.ethz.ast.gdbmeter.common;

import ch.ethz.ast.gdbmeter.common.schema.Schema;

public interface OracleFactory<C extends Connection, T> {

    Oracle createOracle(OracleType type, GlobalState<C> state, Schema<T> schema);

}
