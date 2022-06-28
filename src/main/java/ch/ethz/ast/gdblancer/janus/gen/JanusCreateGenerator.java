package ch.ethz.ast.gdblancer.janus.gen;

import ch.ethz.ast.gdblancer.common.schema.CypherSchema;
import ch.ethz.ast.gdblancer.common.schema.CypherType;
import ch.ethz.ast.gdblancer.util.Randomization;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.Map;
import java.util.Set;

public class JanusCreateGenerator {

    private Graph graph;
    private final CypherSchema schema;

    public JanusCreateGenerator(CypherSchema schema) {
        this.schema = schema;
    }

    public static void createEntities(CypherSchema schema) {
        int numberOfNodes = Randomization.nextInt(1, 6);

        for (int i = 0; i < numberOfNodes; i++) {
            // generateNode();
        }
    }

    private void generateNode() {
        String label = schema.getRandomLabel();

        try (GraphTraversalSource traversal = graph.traversal()) {
            Map<String, CypherType> availableProperties = schema.getEntityByLabel(label).getAvailableProperties();
            Set<String> chosenProperties = Randomization.nonEmptySubset(availableProperties.keySet());
            GraphTraversal<Vertex, Vertex> node = traversal.addV(label);

            for (String property : chosenProperties) {
                CypherType type = availableProperties.get(property);

                // generate value for type
                Object value = 0;

                node.property(property, value);
            }

            traversal.tx().commit();
        } catch (Exception e) {
            // TODO: Rollback TX ?
            e.printStackTrace();
        }

    }

}
