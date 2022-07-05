package ch.ethz.ast.gdblancer.janus;

import ch.ethz.ast.gdblancer.common.Connection;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.util.GraphFactory;
import org.janusgraph.util.system.ConfigurationUtil;

public class JanusConnection implements Connection {

    private Graph graph;

    @Override
    public void connect() throws ConfigurationException {
        PropertiesConfiguration conf = ConfigurationUtil.loadPropertiesConfig("conf/test.properties");
        graph = GraphFactory.open(conf);

    }

    @Override
    public void close() throws Exception {
        graph.close();
    }

}
