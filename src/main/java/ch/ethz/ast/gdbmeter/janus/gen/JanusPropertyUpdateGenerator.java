package ch.ethz.ast.gdbmeter.janus.gen;

import ch.ethz.ast.gdbmeter.common.Query;
import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.janus.JanusConnection;
import ch.ethz.ast.gdbmeter.janus.query.JanusQuery;
import ch.ethz.ast.gdbmeter.janus.schema.JanusType;
import ch.ethz.ast.gdbmeter.janus.schema.Predicate;
import ch.ethz.ast.gdbmeter.janus.schema.PredicateGenerator;
import ch.ethz.ast.gdbmeter.janus.schema.PredicateVisitor;
import ch.ethz.ast.gdbmeter.util.Randomization;

import java.util.Map;

public class JanusPropertyUpdateGenerator {

    private final Schema<JanusType> schema;
    private final StringBuilder query = new StringBuilder();

    public JanusPropertyUpdateGenerator(Schema<JanusType> schema) {
        this.schema = schema;
    }

    public static Query<JanusConnection> updateProperties(Schema<JanusType> schema) {
        return new JanusPropertyUpdateGenerator(schema).generateUpdate();
    }

    private JanusQuery generateUpdate() {
        String label = schema.getRandomLabel();
        Entity<JanusType> entity = schema.getEntityByLabel(label);
        Map<String, JanusType> properties = entity.getAvailableProperties();

        String matchProperty = Randomization.fromSet(properties.keySet());
        String updateProperty = Randomization.fromSet(properties.keySet());

        JanusType matchType = properties.get(matchProperty);
        JanusType updateType = properties.get(updateProperty);

        Predicate predicate = PredicateGenerator.generateFor(matchType);

        query.append(String.format("g.V().hasLabel('%s').has('%s', %s).property(single, '%s', %s).iterate()",
                label,
                matchProperty,
                PredicateVisitor.asString(predicate),
                updateProperty,
                JanusValueGenerator.generate(updateType)));

        return new JanusQuery(query.toString());
    }
}
