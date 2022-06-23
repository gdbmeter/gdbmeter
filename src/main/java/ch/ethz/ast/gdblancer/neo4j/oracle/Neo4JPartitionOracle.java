package ch.ethz.ast.gdblancer.neo4j.oracle;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.cypher.ast.CypherExpression;
import ch.ethz.ast.gdblancer.cypher.oracle.CypherPartitionOracle;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.cypher.schema.CypherType;
import ch.ethz.ast.gdblancer.neo4j.Neo4JBugs;
import ch.ethz.ast.gdblancer.neo4j.Neo4JConnection;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.neo4j.Neo4JUtil;
import ch.ethz.ast.gdblancer.neo4j.ast.Neo4JExpressionGenerator;

import java.util.Map;

public class Neo4JPartitionOracle extends CypherPartitionOracle<Neo4JConnection> {

    private final ExpectedErrors errors = new ExpectedErrors();

    public Neo4JPartitionOracle(GlobalState<Neo4JConnection> state, CypherSchema schema) {
        super(state, schema);

        Neo4JUtil.addRegexErrors(errors);
        Neo4JUtil.addArithmeticErrors(errors);
        Neo4JUtil.addFunctionErrors(errors);
    }

    @Override
    protected CypherExpression getWhereClause(CypherEntity entity) {
        return Neo4JExpressionGenerator.generateExpression(Map.of("n", entity), CypherType.BOOLEAN);
    }

    @Override
    protected Query<Neo4JConnection> makeQuery(String query) {
        return new Neo4JQuery(query, errors);
    }

    @Override
    public void onGenerate() {
        Neo4JBugs.PartitionOracleSpecific.bug12883 = true;
    }

    @Override
    public void onStart() {
        Neo4JBugs.PartitionOracleSpecific.enableAll();
    }

    @Override
    public void onComplete() {
        Neo4JBugs.PartitionOracleSpecific.disableAll();
    }

}
