package ch.ethz.ast.gdblancer.janus.gen;

import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.schema.Index;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.janus.JanusConnection;
import ch.ethz.ast.gdblancer.janus.JanusQueryAdapter;
import ch.ethz.ast.gdblancer.janus.schema.JanusType;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.SchemaAction;
import org.janusgraph.core.schema.SchemaStatus;
import org.janusgraph.graphdb.database.management.ManagementSystem;

import javax.ws.rs.NotSupportedException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class JanusCreateIndexGenerator {

    public static JanusQueryAdapter createIndex(Schema<JanusType> schema) {
        Index index = schema.generateRandomNodeIndex();
        String label = index.getLabel();
        Set<String> properties = index.getPropertyNames();
        String indexName = schema.generateRandomIndexName();

        // TODO: Maybe move to own class -> might be useful for QueryReplay later on
        return new JanusQueryAdapter(true) {
            @Override
            public boolean execute(GlobalState<JanusConnection> globalState) {
                globalState.getLogger().info("Creating composite index {} on label {} and properties {}", indexName, label, properties);

                JanusConnection connection = globalState.getConnection();
                JanusGraph graph = connection.getGraph();
                JanusGraphManagement management = graph.openManagement();

                try {
                    JanusGraphManagement.IndexBuilder builder = management.buildIndex(indexName, Vertex.class);

                    for (String property : properties) {
                        builder = builder.addKey(management.getPropertyKey(property));
                    }

                    builder.indexOnly(management.getVertexLabel(label)).buildCompositeIndex();
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
