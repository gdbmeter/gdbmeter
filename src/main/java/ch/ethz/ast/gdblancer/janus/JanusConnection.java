package ch.ethz.ast.gdblancer.janus;

import ch.ethz.ast.gdblancer.common.Connection;
import ch.ethz.ast.gdblancer.janus.query.JanusQuery;
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

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class JanusConnection implements Connection {

    private JanusGraph graph;
    private GremlinExecutor executor;
    private GraphTraversalSource traversal;

    @Override
    public void connect() throws ConfigurationException {
        PropertiesConfiguration conf = ConfigurationUtil.loadPropertiesConfig("conf/test.properties");
        graph = JanusGraphFactory.open(conf);

        // This is a workaround to be able to use strings as queries
        // An alternative approach would be to create an AST of a query and then map it to the functional Gremlin API
        // However, in that case we also need to provide a toString() version so that we can reproduce bugs
        ConcurrentBindings bindings = new ConcurrentBindings();
        traversal = graph.traversal();
        bindings.put("g", traversal);

        executor = GremlinExecutor.build()
                .evaluationTimeout(3000L)
                .globalBindings(bindings)
                // this makes sure that all queries are executed in the same thread
                // it seems to prevent PermanentLockingExceptions
                .executorService(Executors.newFixedThreadPool(1))
                .create();
    }

    @Override
    public void close() throws Exception {
        JanusGraphFactory.drop(graph);

        executor.close();
        traversal.close();
        graph.close();
    }

    public List<Map<String, Object>> execute(JanusQuery query) throws ExecutionException, InterruptedException {
        CompletableFuture<Object> future = executor.eval(query.getQuery());
        future.get();

        // This is technically not necessary since we run all queries in the same transaction.
        // But it's still necessary when querying properties of our graph from a different thread or client.
        executor.eval("g.tx().commit()").get();
        return null;
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
