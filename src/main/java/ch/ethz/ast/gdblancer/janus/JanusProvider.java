package ch.ethz.ast.gdblancer.janus;

import ch.ethz.ast.gdblancer.common.Generator;
import ch.ethz.ast.gdblancer.common.OracleFactory;
import ch.ethz.ast.gdblancer.common.Provider;
import ch.ethz.ast.gdblancer.common.QueryReplay;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.janus.schema.JanusType;

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
        return null;
    }

    @Override
    public QueryReplay getQueryReplay() {
        return null;
    }
}
