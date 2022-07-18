package ch.ethz.ast.gdblancer.janus.oracle;

import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Oracle;
import ch.ethz.ast.gdblancer.common.schema.Entity;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.janus.JanusConnection;
import ch.ethz.ast.gdblancer.janus.query.JanusQuery;
import ch.ethz.ast.gdblancer.janus.schema.JanusType;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.neo4j.ast.Neo4JExpressionGenerator;
import ch.ethz.ast.gdblancer.neo4j.schema.Neo4JType;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JanusEmptyResultOracle implements Oracle {

    private final GlobalState<JanusConnection> state;
    private final Schema<JanusType> schema;

    public JanusEmptyResultOracle(GlobalState<JanusConnection> state, Schema<JanusType> schema) {
        this.state = state;
        this.schema = schema;
    }

    @Override
    public void check() {
        Set<Long> allIds = new HashSet<>();
        List<Map<String, Object>> idResult = new JanusQuery("MATCH (n) RETURN id(n)").executeAndGet(state);

        for (Map<String, Object> properties : idResult) {
            allIds.add((Long) properties.get("id(n)"));
        }

        if (allIds.isEmpty()) {
            return;
        }

        String label = schema.getRandomLabel();
        Entity<JanusType> entity = schema.getEntityByLabel(label);

        String query = String.format("MATCH (n:%s) WHERE %s RETURN n",
                label,
                CypherVisitor.asString(Neo4JExpressionGenerator.generateExpression(Map.of("n", entity), Neo4JType.BOOLEAN)));

        JanusQuery initialQuery = new JanusQuery(query);

        List<Map<String, Object>> initialResult = initialQuery.executeAndGet(state);

        if (initialResult == null) {
            throw new IgnoreMeException();
        }

        if (initialResult.isEmpty()) {
            for (int i = 0; i < Randomization.smallNumber() && !allIds.isEmpty(); i++) {
                Long chosenId = Randomization.fromSet(allIds);

                new JanusQuery(String.format("MATCH (n) WHERE id(n) = %d DETACH DELETE n", chosenId)).execute(state);
                allIds.remove(chosenId);
            }

            List<Map<String, Object>> result = initialQuery.executeAndGet(state);

            if (result == null) {
                throw new AssertionError("Empty oracle failed because the second query threw an exception");
            }

            if (result.size() != 0) {
                throw new AssertionError(String.format("Empty oracle failed with size: %d", result.size()));
            }
        }

    }
}
