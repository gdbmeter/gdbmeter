package ch.ethz.ast.gdblancer.neo4j.oracle;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.cypher.oracle.CypherNonEmptyResultOracle;
import ch.ethz.ast.gdblancer.common.schema.CypherEntity;
import ch.ethz.ast.gdblancer.common.schema.CypherSchema;
import ch.ethz.ast.gdblancer.common.schema.CypherType;
import ch.ethz.ast.gdblancer.neo4j.Neo4JConnection;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.neo4j.Neo4JUtil;
import ch.ethz.ast.gdblancer.neo4j.ast.Neo4JExpressionGenerator;

import java.util.Map;

public class Neo4JNonEmptyResultOracle extends CypherNonEmptyResultOracle<Neo4JConnection> {

    public Neo4JNonEmptyResultOracle(GlobalState<Neo4JConnection> state, CypherSchema schema) {
        super(state, schema);
    }

    @Override
    protected Query<Neo4JConnection> getIdQuery() {
        return new Neo4JQuery("MATCH (n) RETURN id(n)");
    }

    @Override
    protected Query<Neo4JConnection> getInitialQuery(String label, CypherEntity entity) {
        ExpectedErrors errors = new ExpectedErrors();

        Neo4JUtil.addRegexErrors(errors);
        Neo4JUtil.addArithmeticErrors(errors);
        Neo4JUtil.addFunctionErrors(errors);

        String query = String.format("MATCH (n:%s) WHERE %s RETURN id(n)",
                label,
                CypherVisitor.asString(Neo4JExpressionGenerator.generateExpression(Map.of("n", entity), CypherType.BOOLEAN)));

        return new Neo4JQuery(query, errors);
    }

    @Override
    protected Query<Neo4JConnection> getDeleteQuery(Long id) {
        return new Neo4JQuery(String.format("MATCH (n) WHERE id(n) = %d DETACH DELETE n", id));
    }

}
