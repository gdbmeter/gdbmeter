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

public class JanusDeleteGenerator {

    private final Schema<JanusType> schema;
    private final StringBuilder query = new StringBuilder();

    public JanusDeleteGenerator(Schema<JanusType> schema) {
        this.schema = schema;
    }

    public static Query<JanusConnection> deleteNodes(Schema<JanusType> schema) {
        return new JanusDeleteGenerator(schema).generateDelete();
    }

    private JanusQuery generateDelete() {
        String label = schema.getRandomLabel();
        Entity<JanusType> entity = schema.getEntityByLabel(label);
        Map<String, JanusType> properties = entity.getAvailableProperties();

        String property = Randomization.fromSet(properties.keySet());
        JanusType type = properties.get(property);
        Predicate predicate = PredicateGenerator.generateFor(type);

        query.append(String.format("g.V().hasLabel('%s').has('%s', %s).drop().iterate()",
                label,
                property,
                PredicateVisitor.asString(predicate)));

        return new JanusQuery(query.toString());
    }

}
