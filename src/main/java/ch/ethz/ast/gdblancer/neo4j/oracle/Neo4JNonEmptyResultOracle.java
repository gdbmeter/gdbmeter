package ch.ethz.ast.gdblancer.neo4j.oracle;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Oracle;
import ch.ethz.ast.gdblancer.neo4j.Neo4JConnection;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.cypher.ast.CypherExpression;
import ch.ethz.ast.gdblancer.neo4j.ast.Neo4JExpressionGenerator;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.cypher.schema.CypherType;
import ch.ethz.ast.gdblancer.neo4j.gen.util.Neo4JDBUtil;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Neo4JNonEmptyResultOracle implements Oracle {

    private final GlobalState<Neo4JConnection> state;
    private final CypherSchema schema;

    public Neo4JNonEmptyResultOracle(GlobalState<Neo4JConnection> state, CypherSchema schema) {
        this.state = state;
        this.schema = schema;
    }

    private String randomMatch(String label) {
        CypherEntity entity = schema.getEntityByLabel(label);

        StringBuilder query = new StringBuilder();
        query.append(String.format("MATCH (n:%s)", label));
        query.append(" WHERE ");
        CypherExpression matchingExpression = Neo4JExpressionGenerator.generateExpression(Map.of("n", entity), CypherType.BOOLEAN);
        query.append(CypherVisitor.asString(matchingExpression));

        return query.toString();
    }

    @Override
    public void check() {
        ExpectedErrors errors = new ExpectedErrors();

        Neo4JDBUtil.addRegexErrors(errors);
        Neo4JDBUtil.addArithmeticErrors(errors);
        Neo4JDBUtil.addFunctionErrors(errors);

        Set<Long> allIds = new HashSet<>();
        List<Map<String, Object>> idResult = new Neo4JQuery("MATCH (n) RETURN id(n)").executeAndGet(state);

        for (Map<String, Object> properties : idResult) {
            allIds.add((Long) properties.get("id(n)"));
        }

        String label = schema.getRandomLabel();
        Neo4JQuery initialQuery = new Neo4JQuery(randomMatch(label) + " RETURN id(n)", errors);
        List<Map<String, Object>> initialResult = initialQuery.executeAndGet(state);

        if (initialResult == null) {
            throw new IgnoreMeException();
        }

        int initialSize = initialResult.size();

        if (initialSize != 0) {
            for (Map<String, Object> properties : initialResult) {
                allIds.remove((Long) properties.get("id(n)"));
            }

            if (allIds.isEmpty()) {
                throw new IgnoreMeException();
            }

            for (int i = 0; i < Randomization.smallNumber() && !allIds.isEmpty(); i++) {
                Long chosenId = Randomization.fromSet(allIds);
                new Neo4JQuery(String.format("MATCH (n) WHERE id(n) = %d DETACH DELETE n", chosenId)).execute(state);
                allIds.remove(chosenId);
            }

            List<Map<String, Object>> result = initialQuery.executeAndGet(state);

            if (result == null) {
                throw new AssertionError("Non empty oracle failed because the second query threw an exception");
            }

            if (result.size() != initialSize) {
                throw new AssertionError(String.format("Non empty oracle failed with size: %d, expected was %d", result.size(), initialResult.size()));
            }
        }
    }

}
