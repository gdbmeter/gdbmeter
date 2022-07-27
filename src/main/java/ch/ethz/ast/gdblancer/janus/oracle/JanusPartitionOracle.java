package ch.ethz.ast.gdblancer.janus.oracle;

import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Oracle;
import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.common.schema.Entity;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.janus.JanusConnection;
import ch.ethz.ast.gdblancer.janus.query.JanusQuery;
import ch.ethz.ast.gdblancer.janus.schema.*;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JanusPartitionOracle implements Oracle {

    private final GlobalState<JanusConnection> state;
    private final Schema<JanusType> schema;

    public JanusPartitionOracle(GlobalState<JanusConnection> state, Schema<JanusType> schema) {
        this.state = state;
        this.schema = schema;
    }

    @Override
    public void check() {
        int exceptions = 0;

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

        Set<String> properties = entity.getAvailableProperties().keySet();
        String property = Randomization.fromSet(properties);
        JanusType type = entity.getAvailableProperties().get(property);

        Predicate predicate = PredicateGenerator.generateFor(type);
        String query = "g.V().hasLabel('%s').has('%s', %s).count().next()";
        
        Query<JanusConnection> firstQuery = new JanusQuery(String.format(query, label, property, PredicateVisitor.asString(predicate)));
        result = firstQuery.executeAndGet(state);
        Long first = 0L;

        if (result != null) {
            first = (Long) result.get(0).get("count");
        } else {
            exceptions++;
        }

        Predicate negatedPredicate = new UnaryPredicate(UnaryPredicate.Type.NOT, predicate);
        Query<JanusConnection> secondQuery = new JanusQuery(String.format(query, label, property, PredicateVisitor.asString(negatedPredicate)));

        result = secondQuery.executeAndGet(state);
        Long second = 0L;

        if (result != null) {
            second = (Long) result.get(0).get("count");
        } else {
            exceptions++;
        }

        Query<JanusConnection> thirdQuery = new JanusQuery(String.format("g.V().hasLabel('%s').hasNot('%s').count().next()", label, property));
        result = thirdQuery.executeAndGet(state);
        Long third = 0L;

        if (result != null) {
            third = (Long) result.get(0).get("count");
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

}
