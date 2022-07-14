package ch.ethz.ast.gdblancer.janus.gen;

import ch.ethz.ast.gdblancer.common.schema.Entity;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.janus.query.JanusQuery;
import ch.ethz.ast.gdblancer.janus.query.JanusQueryAdapter;
import ch.ethz.ast.gdblancer.janus.schema.JanusPredicate;
import ch.ethz.ast.gdblancer.janus.schema.JanusType;
import ch.ethz.ast.gdblancer.util.Randomization;

public class JanusDeleteGenerator {

    private final Schema<JanusType> schema;
    private final StringBuilder query = new StringBuilder();

    public JanusDeleteGenerator(Schema<JanusType> schema) {
        this.schema = schema;
    }

    public static JanusQueryAdapter deleteNodes(Schema<JanusType> schema) {
        return new JanusDeleteGenerator(schema).generateDelete();
    }

    private JanusQuery generateDelete() {
        String label = schema.getRandomLabel();
        Entity<JanusType> entity = schema.getEntityByLabel(label);
        String property = Randomization.fromSet(entity.getAvailableProperties().keySet());

        JanusType type = entity.getAvailableProperties().get(property);
        JanusPredicate predicate = JanusPredicate.compareTo(type);

        query.append(String.format("g.V().hasLabel('%s').has('%s', %s).drop().iterate()",
                label,
                property,
                predicate.toString(JanusValueGenerator.generate(type))));

        return new JanusQuery(query.toString());
    }

}
