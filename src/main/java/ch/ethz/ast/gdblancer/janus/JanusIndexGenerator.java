package ch.ethz.ast.gdblancer.janus;

import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.schema.Index;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.VertexLabel;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.SchemaAction;
import org.janusgraph.core.schema.SchemaStatus;
import org.janusgraph.graphdb.database.management.ManagementSystem;

import javax.ws.rs.NotSupportedException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class JanusIndexGenerator {

    public static JanusQueryAdapter createIndex(Schema<JanusType> schema) {
        Index index = schema.generateRandomNodeIndex();
        String label = index.getLabel();
        Set<String> properties = index.getPropertyNames();
        String indexName = schema.generateRandomIndexName();

        return new JanusQueryAdapter(true) {
            @Override
            public boolean execute(GlobalState<JanusConnection> globalState) {
                JanusConnection connection = globalState.getConnection();
                JanusGraph graph = connection.getGraph();
                JanusGraphManagement management = graph.openManagement();

                try {
                    PropertyKey propertyKey = management.getPropertyKey(properties.toArray(new String[]{})[0]);
                    VertexLabel vertexLabel = management.getVertexLabel(label);

                    management.buildIndex(indexName, Vertex.class)
                            .addKey(propertyKey)
                            .indexOnly(vertexLabel)
                            .buildCompositeIndex();

                    management.commit();

                    // Wait for the index to be created
                    ManagementSystem.awaitGraphIndexStatus(graph, indexName).
                            status(SchemaStatus.REGISTERED)
                            .call();

                    // Update the index
                    management = graph.openManagement();
                    management.updateIndex(management.getGraphIndex(indexName), SchemaAction.REINDEX).get();
                    management.commit();
                } catch (InterruptedException | ExecutionException e) {
                    return false;
                }

                return true;
            }

            @Override
            public String getQuery() {
                throw new NotSupportedException();
            }

            @Override
            public List<Map<String, Object>> executeAndGet(GlobalState<JanusConnection> globalState) {
                throw new NotSupportedException();
            }
        };
    }

}
