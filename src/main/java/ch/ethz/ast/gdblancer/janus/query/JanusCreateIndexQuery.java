package ch.ethz.ast.gdblancer.janus.query;

import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.janus.JanusConnection;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.schema.JanusGraphIndex;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.SchemaAction;
import org.janusgraph.core.schema.SchemaStatus;
import org.janusgraph.graphdb.database.management.ManagementSystem;

import javax.ws.rs.NotSupportedException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class JanusCreateIndexQuery extends JanusQueryAdapter {

    private final String label;
    private final Set<String> properties;
    private final String indexName;

    public JanusCreateIndexQuery(String label, Set<String> properties, String indexName) {
        super(true);

        this.label = label;
        this.properties = properties;
        this.indexName = indexName;
    }

    @Override
    public boolean execute(GlobalState<JanusConnection> globalState) {
        globalState.getLogger().info("Creating mixed index {} on label {} and properties {}", indexName, label, properties);

        JanusConnection connection = globalState.getConnection();
        JanusGraph graph = connection.getGraph();
        JanusGraphManagement management = graph.openManagement();

        try {
            JanusGraphManagement.IndexBuilder builder = management.buildIndex(indexName, Vertex.class);

            for (String property : properties) {
                builder = builder.addKey(management.getPropertyKey(property));
            }

            builder.indexOnly(management.getVertexLabel(label)).buildMixedIndex("search");
            management.commit();

            // Wait for the index to be created
            ManagementSystem.awaitGraphIndexStatus(graph, indexName).
                    status(SchemaStatus.REGISTERED)
                    .call();

            management = graph.openManagement();
            JanusGraphIndex graphIndex = management.getGraphIndex(indexName);

            if (graphIndex == null) {
                // In this case we probably timed out whilst waiting for the REGISTERED state.
                return false;
            }

            // Update the index
            management.updateIndex(graphIndex, SchemaAction.REINDEX).get();
            management.commit();
        } catch (InterruptedException | ExecutionException e) {
            management.rollback();
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
}
