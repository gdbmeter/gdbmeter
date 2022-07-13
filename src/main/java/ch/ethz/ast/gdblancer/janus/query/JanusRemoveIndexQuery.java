package ch.ethz.ast.gdblancer.janus.query;

import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.janus.JanusConnection;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.SchemaAction;
import org.janusgraph.core.schema.SchemaStatus;
import org.janusgraph.graphdb.database.management.ManagementSystem;

import javax.ws.rs.NotSupportedException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class JanusRemoveIndexQuery extends JanusQueryAdapter {

    private final String indexName;

    public JanusRemoveIndexQuery(String indexName) {
        super(true);
        this.indexName = indexName;
    }

    @Override
    public boolean execute(GlobalState<JanusConnection> globalState) {
        globalState.getLogger().info("Deleting composite index {}", indexName);

        JanusConnection connection = globalState.getConnection();
        JanusGraph graph = connection.getGraph();
        JanusGraphManagement management = graph.openManagement();

        try {
            management.updateIndex(management.getGraphIndex(indexName), SchemaAction.DISABLE_INDEX).get();
            management.commit();

            ManagementSystem.awaitGraphIndexStatus(graph, indexName).status(SchemaStatus.DISABLED).call();

            management = graph.openManagement();
            management.updateIndex(management.getGraphIndex(indexName), SchemaAction.REMOVE_INDEX).get();
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
