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
    private final boolean composite; // if not then the index is mixed

    public JanusCreateIndexQuery(String label, Set<String> properties, String indexName, boolean composite) {
        super(true);

        this.label = label;
        this.properties = properties;
        this.indexName = indexName;
        this.composite = composite;
    }

    @Override
    public boolean execute(GlobalState<JanusConnection> globalState) {
        globalState.getLogger().info("Creating {} index {} on label {} and properties {}", composite ? "composite" : "mixed", indexName, label, properties);

        JanusConnection connection = globalState.getConnection();
        JanusGraph graph = connection.getGraph();
        JanusGraphManagement management = graph.openManagement();

        try {
            JanusGraphManagement.IndexBuilder builder = management.buildIndex(indexName, Vertex.class);

            for (String property : properties) {
                builder = builder.addKey(management.getPropertyKey(property));
            }

            // TODO: Also support composite indices
            builder = builder.indexOnly(management.getVertexLabel(label));

            if (composite) {
                builder.buildCompositeIndex();
            } else {
                builder.buildMixedIndex("search");
            }

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
            JanusGraphManagement.IndexJobFuture future = management.updateIndex(graphIndex, SchemaAction.REINDEX);

            if (future == null) {
                globalState.getLogger().warn("The returned future from updateIndex is null");
                return false;
            }

            future.get();
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
