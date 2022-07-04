package ch.ethz.ast.gdblancer.common;

import ch.ethz.ast.gdblancer.common.schema.Schema;

public interface Provider<C extends Connection, T> {

    C getConnection();
    Schema<T> getSchema();
    Generator<C> getGenerator(Schema<T> schema);
    OracleFactory<C, T> getOracleFactory();
    QueryReplay getQueryReplay();

}
