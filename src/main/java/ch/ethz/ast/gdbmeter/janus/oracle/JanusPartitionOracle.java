package ch.ethz.ast.gdbmeter.janus.oracle;

import ch.ethz.ast.gdbmeter.common.GlobalState;
import ch.ethz.ast.gdbmeter.common.Oracle;
import ch.ethz.ast.gdbmeter.common.Query;
import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.janus.JanusBugs;
import ch.ethz.ast.gdbmeter.janus.JanusConnection;
import ch.ethz.ast.gdbmeter.janus.query.JanusQuery;
import ch.ethz.ast.gdbmeter.janus.schema.*;
import ch.ethz.ast.gdbmeter.util.IgnoreMeException;
import ch.ethz.ast.gdbmeter.util.Randomization;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record JanusPartitionOracle(
        GlobalState<JanusConnection> state,
        Schema<JanusType> schema) implements Oracle {

    @Override
    public void check() {
        String label = schema.getRandomLabel();
        Entity<JanusType> entity = schema.getEntityByLabel(label);

        Query<JanusConnection> initialQuery = new JanusQuery(String.format("g.V().hasLabel('%s').count().next()", label));
        List<Map<String, Object>> result = initialQuery.executeAndGet(state);
        Long expectedTotal;

        if (result != null) {
            expectedTotal = (Long) result.get(0).get("count");
        } else {
            throw new AssertionError("Unexpected exception when fetching total");
        }

        Set<String> properties = entity.availableProperties().keySet();
        String property = Randomization.fromSet(properties);
        JanusType type = entity.availableProperties().get(property);

        if (JanusBugs.bug6578 && type == JanusType.UUID) {
            throw new IgnoreMeException();
        }

        Predicate predicate = PredicateGenerator.generateFor(type);
        String query = "g.V().hasLabel('%s').has('%s', %s).count().next()";

        Query<JanusConnection> firstQuery = new JanusQuery(String.format(query, label, property, PredicateVisitor.asString(predicate)));
        result = firstQuery.executeAndGet(state);
        Long first;

        if (result != null) {
            first = (Long) result.get(0).get("count");
        } else {
            throw new AssertionError("Unexpected exception");
        }

        Predicate negatedPredicate = new UnaryPredicate(UnaryPredicate.Type.NOT, predicate);
        Query<JanusConnection> secondQuery = new JanusQuery(String.format(query, label, property, PredicateVisitor.asString(negatedPredicate)));

        result = secondQuery.executeAndGet(state);
        Long second;

        if (result != null) {
            second = (Long) result.get(0).get("count");
        } else {
            throw new AssertionError("Unexpected exception");
        }

        Query<JanusConnection> thirdQuery = new JanusQuery(String.format("g.V().hasLabel('%s').hasNot('%s').count().next()", label, property));
        result = thirdQuery.executeAndGet(state);
        Long third;

        if (result != null) {
            third = (Long) result.get(0).get("count");
        } else {
            throw new AssertionError("Unexpected exception");
        }

        if (first + second + third != expectedTotal) {
            throw new AssertionError(String.format("%d + %d + %d is not equal to %d", first, second, third, expectedTotal));
        }
    }

}
