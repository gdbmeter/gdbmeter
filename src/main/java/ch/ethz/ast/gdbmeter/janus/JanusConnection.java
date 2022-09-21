package ch.ethz.ast.gdbmeter.janus;

import ch.ethz.ast.gdbmeter.common.Connection;
import ch.ethz.ast.gdbmeter.janus.query.JanusQuery;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.tinkerpop.gremlin.groovy.engine.GremlinExecutor;
import org.apache.tinkerpop.gremlin.jsr223.ConcurrentBindings;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.schema.JanusGraphIndex;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.SchemaStatus;
import org.janusgraph.util.system.ConfigurationUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JanusConnection implements Connection {

    private JanusGraph graph;
    private GremlinExecutor executor;
    private GraphTraversalSource traversal;
    private ExecutorService executorService;

    @Override
    public void connect() throws ConfigurationException {
        PropertiesConfiguration conf = ConfigurationUtil.loadPropertiesConfig("test.properties");
        graph = JanusGraphFactory.open(conf);

        // This is a workaround to be able to use strings as queries
        // An alternative approach would be to create an AST of a query and then map it to the functional Gremlin API
        // However, in that case we also need to provide a toString() version so that we can reproduce bugs
        // See also: https://github.com/JanusGraph/janusgraph/pull/3149
        ConcurrentBindings bindings = new ConcurrentBindings();
        traversal = graph.traversal();
        bindings.put("g", traversal);

        executorService = Executors.newFixedThreadPool(1);
        executor = GremlinExecutor.build()
                .evaluationTimeout(4000L)
                .globalBindings(bindings)
                // this makes sure that all queries are executed in the same thread
                // it seems to prevent PermanentLockingExceptions
                .executorService(executorService)
                .create();
    }

    @Override
    public void close() throws Exception {
        JanusGraphFactory.drop(graph);

        executor.close();
        traversal.close();
        graph.close();

        // Since we supply our own executor service, we are responsible for closing it
        executorService.shutdown();
    }

    public List<Map<String, Object>> execute(JanusQuery query) throws ExecutionException, InterruptedException {
        CompletableFuture<Object> future = executor.eval(query.getQuery());
        Object result = future.get();

        List<Map<String, Object>> realResult = null;

        if (result instanceof List<?>) {
            realResult = (List<Map<String, Object>>) result;
        } else if (result instanceof Long) {
            realResult = List.of(Map.of("count", result));
        }

        // This is technically not necessary since we run all queries in the same transaction.
        // But it's still necessary when querying properties of our graph from a different thread or client.
        executor.eval("g.tx().commit()").get();

        return realResult;
    }

    public JanusGraph getGraph() {
        return graph;
    }

    public Set<String> getIndexNames() {
        Set<String> names = new HashSet<>();
        JanusGraphManagement management = graph.openManagement();

        for (JanusGraphIndex index : management.getGraphIndexes(Vertex.class)) {
            // If the index is enabled it should have status ENABLED on any field
            // And there is always at least one field
            if (index.getIndexStatus(index.getFieldKeys()[0]) == SchemaStatus.ENABLED) {
                names.add(index.name());
            }
        }

        // This is mandatory because otherwise we have an open transaction which causes
        // problems when creating indices!
        management.commit();

        return names;
    }
}
