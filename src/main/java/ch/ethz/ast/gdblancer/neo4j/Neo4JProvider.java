package ch.ethz.ast.gdblancer.neo4j;

import ch.ethz.ast.gdblancer.common.Generator;
import ch.ethz.ast.gdblancer.common.OracleFactory;
import ch.ethz.ast.gdblancer.common.Provider;
import ch.ethz.ast.gdblancer.common.QueryReplay;
import ch.ethz.ast.gdblancer.common.schema.CypherSchema;
import ch.ethz.ast.gdblancer.neo4j.oracle.Neo4JOracleFactory;

public class Neo4JProvider implements Provider<Neo4JConnection> {

    @Override
    public Neo4JConnection getConnection() {
        return new Neo4JConnection();
    }

    @Override
    public Generator<Neo4JConnection> getGenerator(CypherSchema schema) {
        return new Neo4JGenerator(schema);
    }

    @Override
    public OracleFactory<Neo4JConnection> getOracleFactory() {
        return new Neo4JOracleFactory();
    }

    @Override
    public QueryReplay getQueryReplay() {
        return new Neo4JQueryReplay();
    }

}
