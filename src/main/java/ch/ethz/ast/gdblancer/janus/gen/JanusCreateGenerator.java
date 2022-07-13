package ch.ethz.ast.gdblancer.janus.gen;

import ch.ethz.ast.gdblancer.common.schema.Entity;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.cypher.CypherUtil;
import ch.ethz.ast.gdblancer.janus.query.JanusQuery;
import ch.ethz.ast.gdblancer.janus.schema.JanusType;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JanusCreateGenerator {

    private final Schema<JanusType> schema;
    private final StringBuilder query = new StringBuilder();
    private final Set<String> nodeVariables = new HashSet<>();

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!#<>/.,~-+'*()[]{} ^*?%_\t\r|&\\";

    public JanusCreateGenerator(Schema<JanusType> schema) {
        this.schema = schema;
    }

    public static JanusQuery createEntities(Schema<JanusType> schema) {
        return new JanusCreateGenerator(schema).generateAddV();
    }

    private JanusQuery generateAddV() {
        query.append("g");

        for (int i = 0; i < Randomization.nextInt(1, 6); i++) {
            generateNode();
        }

        for (String from : nodeVariables) {
            for (String to : nodeVariables) {
                if (Randomization.getBoolean()) {
                    generateEdge(from, to);
                }
            }
        }

        query.append(".next()");

        return new JanusQuery(query.toString());
    }

    private void generateNode() {
        String label = schema.getRandomLabel();
        Entity<JanusType> entity = schema.getEntityByLabel(label);
        Set<String> selectedProperties = Randomization.nonEmptySubset(entity.getAvailableProperties().keySet());

        query.append(String.format(".addV('%s')", label));

        generateProperties(selectedProperties, entity.getAvailableProperties());

        String name = Randomization.generateUniqueElement(nodeVariables, CypherUtil::generateValidName);
        nodeVariables.add(name);

        query.append(String.format(".as('%s')", name));
    }

    private void generateProperties(Set<String> properties, Map<String, JanusType> types) {
        for (String property : properties) {
            JanusType type = types.get(property);

            query.append(String.format(".property('%s', ", property));
            query.append(JanusValueGenerator.generate(type));
            query.append(")");
        }
    }

    private void generateEdge(String from, String to) {
        String type = schema.getRandomType();
        Entity<JanusType> entity = schema.getEntityByType(type);
        Set<String> selectedProperties = Randomization.nonEmptySubset(entity.getAvailableProperties().keySet());

        query.append(String.format(".addE('%s')", type));
        query.append(String.format(".from('%s')", from));
        query.append(String.format(".to('%s')", to));

        generateProperties(selectedProperties, entity.getAvailableProperties());
    }

}
