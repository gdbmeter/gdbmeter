package ch.ethz.ast.gdblancer.janus;

import ch.ethz.ast.gdblancer.common.schema.Entity;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Set;

public class JanusCreateGenerator {
    
    public static JanusQuery createEntities(Schema<JanusType> schema) {
        String label = schema.getRandomLabel();
        Entity<JanusType> entity = schema.getEntityByLabel(label);
        Set<String> selectedProperties = Randomization.nonEmptySubset(entity.getAvailableProperties().keySet());

        StringBuilder query = new StringBuilder(String.format("g.addV('%s')", label));

        for (String property : selectedProperties) {
            JanusType type = entity.getAvailableProperties().get(property);

            switch (type) {
                case STRING:
                    query.append(String.format(".property('%s', '%s')", property, Randomization.getString()));
                    break;
                case CHARACTER:
                    query.append(String.format(".property('%s', '%s')", property, Randomization.getCharacter()));
                    break;
                case BOOLEAN:
                    query.append(String.format(".property('%s', '%s')", property, Randomization.getBoolean()));
                    break;
                default:
                    throw new AssertionError(type);
            }
        }

        return new JanusQuery(query.toString());
    }

}
