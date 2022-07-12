package ch.ethz.ast.gdblancer.janus.gen;

import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.janus.JanusConnection;
import ch.ethz.ast.gdblancer.janus.JanusQueryAdapter;
import ch.ethz.ast.gdblancer.janus.schema.JanusType;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.SchemaAction;
import org.janusgraph.core.schema.SchemaStatus;
import org.janusgraph.graphdb.database.management.ManagementSystem;

import javax.ws.rs.NotSupportedException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class JanusRemoveIndexGenerator {

    public static JanusQueryAdapter dropIndex(Schema<JanusType> schema) {
        if (!schema.hasIndices()) {
            throw new IgnoreMeException();
        }

        String indexName = schema.getRandomIndex();

        // TODO: Maybe move to own class -> might be useful for QueryReplay later on
        return new JanusQueryAdapter(true) {
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
        };
    }

}
