package ch.ethz.ast.gdblancer.janus.gen;

import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.common.schema.Entity;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.janus.JanusConnection;
import ch.ethz.ast.gdblancer.janus.query.JanusQuery;
import ch.ethz.ast.gdblancer.janus.schema.JanusType;
import ch.ethz.ast.gdblancer.janus.schema.Predicate;
import ch.ethz.ast.gdblancer.janus.schema.PredicateGenerator;
import ch.ethz.ast.gdblancer.janus.schema.PredicateVisitor;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Map;

public class JanusPropertyRemoveGenerator {

    private final Schema<JanusType> schema;
    private final StringBuilder query = new StringBuilder();

    public JanusPropertyRemoveGenerator(Schema<JanusType> schema) {
        this.schema = schema;
    }

    public static Query<JanusConnection> removeProperties(Schema<JanusType> schema) {
        return new JanusPropertyRemoveGenerator(schema).generateRemove();
    }

    private JanusQuery generateRemove() {
        String label = schema.getRandomLabel();
        Entity<JanusType> entity = schema.getEntityByLabel(label);
        Map<String, JanusType> properties = entity.getAvailableProperties();

        String matchProperty = Randomization.fromSet(properties.keySet());
        String removeProperty = Randomization.fromSet(properties.keySet());
        JanusType matchType = properties.get(matchProperty);
        Predicate predicate = PredicateGenerator.generateFor(matchType);

        query.append(String.format("g.V().hasLabel('%s').has('%s', %s).properties('%s').drop().iterate()",
                label,
                matchProperty,
                PredicateVisitor.asString(predicate),
                removeProperty));

        return new JanusQuery(query.toString());
    }
}
