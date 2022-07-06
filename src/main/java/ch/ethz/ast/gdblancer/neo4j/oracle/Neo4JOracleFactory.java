package ch.ethz.ast.gdblancer.neo4j.oracle;

import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Oracle;
import ch.ethz.ast.gdblancer.common.OracleFactory;
import ch.ethz.ast.gdblancer.common.OracleType;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.neo4j.Neo4JConnection;
import ch.ethz.ast.gdblancer.neo4j.schema.Neo4JType;

public class Neo4JOracleFactory implements OracleFactory<Neo4JConnection, Neo4JType> {

    @Override
    public Oracle createOracle(OracleType type, GlobalState<Neo4JConnection> state, Schema<Neo4JType> schema) {
        switch (type) {
            case EMPTY_RESULT:
                return new Neo4JEmptyResultOracle(state, schema);
            case NON_EMPTY_RESULT:
                return new Neo4JNonEmptyResultOracle(state, schema);
            case PARTITION:
                return new Neo4JPartitionOracle(state, schema);
            case REFINEMENT:
                return new Neo4JRefinementOracle(state, schema);
            default:
                throw new AssertionError(type);
        }
    }

}