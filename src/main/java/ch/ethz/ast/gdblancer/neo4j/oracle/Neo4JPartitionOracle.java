package ch.ethz.ast.gdblancer.neo4j.oracle;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Oracle;
import ch.ethz.ast.gdblancer.neo4j.Neo4JConnection;
import ch.ethz.ast.gdblancer.neo4j.gen.Neo4JPropertyGenerator;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBEntity;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JType;
import ch.ethz.ast.gdblancer.neo4j.gen.util.Neo4JDBUtil;
import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JPartitionOracle implements Oracle {

    private final GlobalState<Neo4JConnection> state;
    private final Neo4JDBSchema schema;

    public Neo4JPartitionOracle(GlobalState<Neo4JConnection> state, Neo4JDBSchema schema) {
        this.state = state;
        this.schema = schema;
    }

    @Override
    public void check() {
        ExpectedErrors errors = new ExpectedErrors();

        Neo4JDBUtil.addRegexErrors(errors);
        Neo4JDBUtil.addArithmeticErrors(errors);
        Neo4JDBUtil.addFunctionErrors(errors);

        // Select all nodes with label and random property
        String label = schema.getRandomLabel();
        Neo4JDBEntity entity = schema.getEntityByLabel(label);
        String property = Randomization.fromSet(entity.getAvailableProperties().keySet());
        Neo4JType type = entity.getAvailableProperties().get(property);

        StringBuilder query = new StringBuilder();

        query.append(String.format("MATCH (n:%s ", label));
        query.append(Neo4JPropertyGenerator.generatePropertyQuery(entity));
        query.append(") RETURN n");
    }

}
