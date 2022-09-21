package ch.ethz.ast.gdbmeter.common;

import ch.ethz.ast.gdbmeter.common.schema.Schema;

public interface Provider<C extends Connection, T> {

    C getConnection();
    Schema<T> getSchema();
    Generator<C> getGenerator(Schema<T> schema);
    OracleFactory<C, T> getOracleFactory();
    QueryReplay getQueryReplay();

}
