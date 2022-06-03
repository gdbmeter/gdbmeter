package ch.ethz.ast.gdblancer.neo4j.oracle;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Oracle;
import ch.ethz.ast.gdblancer.neo4j.Neo4JBugs;
import ch.ethz.ast.gdblancer.neo4j.Neo4JConnection;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.cypher.ast.CypherExpression;
import ch.ethz.ast.gdblancer.neo4j.ast.Neo4JExpressionGenerator;
import ch.ethz.ast.gdblancer.cypher.ast.CypherPrefixOperation;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.cypher.schema.CypherType;
import ch.ethz.ast.gdblancer.neo4j.gen.util.Neo4JDBUtil;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;

import java.util.List;
import java.util.Map;

public class Neo4JPartitionOracle implements Oracle {

    private final GlobalState<Neo4JConnection> state;
    private final CypherSchema schema;

    public Neo4JPartitionOracle(GlobalState<Neo4JConnection> state, CypherSchema schema) {
        this.state = state;
        this.schema = schema;
    }

    @Override
    public void check() {
        int exceptions = 0;
        ExpectedErrors errors = new ExpectedErrors();

        Neo4JDBUtil.addRegexErrors(errors);
        Neo4JDBUtil.addArithmeticErrors(errors);
        Neo4JDBUtil.addFunctionErrors(errors);

        String label = schema.getRandomLabel();
        CypherEntity entity = schema.getEntityByLabel(label);

        Neo4JQuery initialQuery = new Neo4JQuery(String.format("MATCH (n:%s) RETURN COUNT(n)", label));
        List<Map<String, Object>> result = initialQuery.executeAndGet(state);
        Long expectedTotal = 0L;

        if (result != null) {
            expectedTotal = (Long) result.get(0).get("COUNT(n)");
        } else {
            throw new AssertionError("Unexpected exception when fetching total");
        }

        StringBuilder query = new StringBuilder();
        query.append(String.format("MATCH (n:%s)", label));
        query.append(" WHERE ");

        CypherExpression whereCondition = Neo4JExpressionGenerator.generateExpression(Map.of("n", entity), CypherType.BOOLEAN);
        query.append(CypherVisitor.asString(whereCondition));
        query.append(" RETURN COUNT(n)");

        Neo4JQuery firstQuery = new Neo4JQuery(query.toString(), errors);
        result = firstQuery.executeAndGet(state);
        Long first = 0L;

        if (result != null) {
            first = (Long) result.get(0).get("COUNT(n)");
        } else {
            exceptions++;
        }

        query = new StringBuilder();
        query.append(String.format("MATCH (n:%s)", label));
        query.append(" WHERE ");

        query.append(CypherVisitor.asString(new CypherPrefixOperation(whereCondition, CypherPrefixOperation.PrefixOperator.NOT)));
        query.append(" RETURN COUNT(n)");

        Neo4JQuery secondQuery = new Neo4JQuery(query.toString(), errors);
        result = secondQuery.executeAndGet(state);
        Long second = 0L;

        if (result != null) {
            second = (Long) result.get(0).get("COUNT(n)");
        } else {
            exceptions++;
        }

        query = new StringBuilder();
        query.append(String.format("MATCH (n:%s)", label));
        query.append(" WHERE (");

        query.append(CypherVisitor.asString(whereCondition));
        query.append(") IS NULL RETURN COUNT(n)");

        Neo4JQuery thirdQuery = new Neo4JQuery(query.toString(), errors);
        result = thirdQuery.executeAndGet(state);
        Long third = 0L;

        if (result != null) {
            third = (Long) result.get(0).get("COUNT(n)");
        } else {
            exceptions++;
        }

        if (exceptions > 0) {
            throw new IgnoreMeException();
        }

        if (first + second + third != expectedTotal) {
            throw new AssertionError(String.format("%d + %d + %d is not equal to %d", first, second, third, expectedTotal));
        }
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
