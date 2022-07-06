package ch.ethz.ast.gdblancer.janus;

import ch.ethz.ast.gdblancer.common.Connection;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.tinkerpop.gremlin.groovy.engine.GremlinExecutor;
import org.apache.tinkerpop.gremlin.jsr223.ConcurrentBindings;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.util.GraphFactory;
import org.janusgraph.util.system.ConfigurationUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class JanusConnection implements Connection {

    private Graph graph;
    private GremlinExecutor executor;
    private GraphTraversalSource traversal;

    @Override
    public void connect() throws ConfigurationException {
        PropertiesConfiguration conf = ConfigurationUtil.loadPropertiesConfig("conf/test.properties");
        graph = GraphFactory.open(conf);

        ConcurrentBindings bindings = new ConcurrentBindings();
        traversal = graph.traversal();
        bindings.putIfAbsent("g", traversal);

        executor = GremlinExecutor.build()
                .evaluationTimeout(2000L)
                .globalBindings(bindings)
                .create();
    }

    @Override
    public void close() throws Exception {
        executor.close();
        traversal.close();
        graph.close();
    }

    public List<Map<String, Object>> execute(JanusQuery query) throws ExecutionException, InterruptedException {
        CompletableFuture<Object> future = executor.eval(query.getQuery());
        future.get();
        return null;
    }
}