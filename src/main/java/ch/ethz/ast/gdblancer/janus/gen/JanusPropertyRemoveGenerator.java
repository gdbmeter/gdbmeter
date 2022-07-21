package ch.ethz.ast.gdblancer.janus.gen;

import ch.ethz.ast.gdblancer.common.schema.Entity;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.janus.query.JanusQuery;
import ch.ethz.ast.gdblancer.janus.query.JanusQueryAdapter;
import ch.ethz.ast.gdblancer.janus.schema.JanusType;
import ch.ethz.ast.gdblancer.janus.schema.Predicate;
import ch.ethz.ast.gdblancer.janus.schema.PredicateGenerator;
import ch.ethz.ast.gdblancer.janus.schema.PredicateVisitor;
import ch.ethz.ast.gdblancer.util.Randomization;

public class JanusPropertyRemoveGenerator {

    private final Schema<JanusType> schema;

    private final StringBuilder query = new StringBuilder();
    public JanusPropertyRemoveGenerator(Schema<JanusType> schema) {
        this.schema = schema;
    }

    public static JanusQueryAdapter removeProperties(Schema<JanusType> schema) {
        return new JanusPropertyRemoveGenerator(schema).generateRemove();
    }

    private JanusQuery generateRemove() {
        String label = schema.getRandomLabel();
        Entity<JanusType> entity = schema.getEntityByLabel(label);
        String matchProperty = Randomization.fromSet(entity.getAvailableProperties().keySet());
        String removeProperty = Randomization.fromSet(entity.getAvailableProperties().keySet());

        JanusType matchType = entity.getAvailableProperties().get(matchProperty);

        Predicate predicate = PredicateGenerator.generateFor(matchType);

        query.append(String.format("g.V().hasLabel('%s').has('%s', %s).properties('%s').drop().iterate()",
                label,
                matchProperty,
                PredicateVisitor.asString(predicate),
                removeProperty));

        return new JanusQuery(query.toString());
    }
}
