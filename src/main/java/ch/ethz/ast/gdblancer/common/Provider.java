package ch.ethz.ast.gdblancer.common;

import ch.ethz.ast.gdblancer.common.schema.CypherSchema;

public interface Provider<C extends Connection> {

    C getConnection();
    Generator<C> getGenerator(CypherSchema schema);
    OracleFactory<C> getOracleFactory();
    QueryReplay getQueryReplay();

}
