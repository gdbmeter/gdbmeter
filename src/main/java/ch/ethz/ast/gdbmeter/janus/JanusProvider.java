package ch.ethz.ast.gdbmeter.janus;

import ch.ethz.ast.gdbmeter.common.Generator;
import ch.ethz.ast.gdbmeter.common.OracleFactory;
import ch.ethz.ast.gdbmeter.common.Provider;
import ch.ethz.ast.gdbmeter.common.QueryReplay;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.janus.oracle.JanusOracleFactory;
import ch.ethz.ast.gdbmeter.janus.schema.JanusType;

import java.util.Set;

public class JanusProvider implements Provider<JanusConnection, JanusType> {

    @Override
    public JanusConnection getConnection() {
        return new JanusConnection();
    }

    @Override
    public Schema<JanusType> getSchema() {
        return Schema.generateRandomSchema(Set.of(JanusType.values()));
    }

    @Override
    public Generator<JanusConnection> getGenerator(Schema<JanusType> schema) {
        return new JanusGenerator(schema);
    }

    @Override
    public OracleFactory<JanusConnection, JanusType> getOracleFactory() {
        return new JanusOracleFactory();
    }

    @Override
    public QueryReplay getQueryReplay() {
        return new JanusQueryReplay();
    }
}
