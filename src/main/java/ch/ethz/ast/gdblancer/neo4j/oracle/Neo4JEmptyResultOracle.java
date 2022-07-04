package ch.ethz.ast.gdblancer.neo4j.oracle;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.cypher.oracle.CypherEmptyResultOracle;
import ch.ethz.ast.gdblancer.common.schema.Entity;
import ch.ethz.ast.gdblancer.neo4j.Neo4JConnection;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.neo4j.Neo4JUtil;
import ch.ethz.ast.gdblancer.neo4j.ast.Neo4JExpressionGenerator;
import ch.ethz.ast.gdblancer.neo4j.schema.Neo4JType;

import java.util.Map;

public class Neo4JEmptyResultOracle extends CypherEmptyResultOracle<Neo4JConnection, Neo4JType> {

    public Neo4JEmptyResultOracle(GlobalState<Neo4JConnection> state, Schema<Neo4JType> schema) {
        super(state, schema);
    }

    @Override
    protected Query<Neo4JConnection> getIdQuery() {
        return new Neo4JQuery("MATCH (n) RETURN id(n)");
    }

    @Override
    protected Query<Neo4JConnection> getInitialQuery(String label, Entity<Neo4JType> entity) {
        ExpectedErrors errors = new ExpectedErrors();

        Neo4JUtil.addRegexErrors(errors);
        Neo4JUtil.addArithmeticErrors(errors);
        Neo4JUtil.addFunctionErrors(errors);

        String query = String.format("MATCH (n:%s) WHERE %s RETURN n",
                label,
                CypherVisitor.asString(Neo4JExpressionGenerator.generateExpression(Map.of("n", entity), Neo4JType.BOOLEAN)));

        return new Neo4JQuery(query, errors);
    }

    @Override
    protected Query<Neo4JConnection> getDeleteQuery(Long id) {
        return new Neo4JQuery(String.format("MATCH (n) WHERE id(n) = %d DETACH DELETE n", id));
    }

}
