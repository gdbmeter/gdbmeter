package ch.ethz.ast.gdblancer.janus;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.util.GraphFactory;
import org.janusgraph.util.system.ConfigurationUtil;

public class Test {

    public static void main(String[] args) throws ConfigurationException {
        PropertiesConfiguration conf = ConfigurationUtil.loadPropertiesConfig("conf/test.properties");
        Graph graph = GraphFactory.open(conf);

        try (GraphTraversalSource g = graph.traversal()) {
            Object herculesAge = g.V().has("name", "hercules").values("age").next();
            System.out.println("Hercules is " + herculesAge + " years old.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
