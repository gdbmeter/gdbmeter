package ch.ethz.ast.gdblancer.janus.query;

import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.janus.JanusConnection;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.SchemaAction;
import org.janusgraph.core.schema.SchemaStatus;
import org.janusgraph.graphdb.database.management.ManagementSystem;

import javax.ws.rs.NotSupportedException;
import java.io.File;
import java.util.Arrays;
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
        globalState.getLogger().info("Deleting mixed index {}", indexName);

        JanusConnection connection = globalState.getConnection();
        JanusGraph graph = connection.getGraph();
        JanusGraphManagement management = graph.openManagement();

        try {
            management.updateIndex(management.getGraphIndex(indexName), SchemaAction.DISABLE_INDEX).get();
            management.commit();

            ManagementSystem.awaitGraphIndexStatus(graph, indexName).status(SchemaStatus.DISABLED).call();

            // To delete the index from the backend we simply delete the appropriate folder
            File indexFolder = new File("data/searchindex/" + indexName);
            File[] folderContents = indexFolder.listFiles();

            if (folderContents != null) {
                Arrays.stream(folderContents).forEach(File::delete);
            }

            indexFolder.delete();
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
