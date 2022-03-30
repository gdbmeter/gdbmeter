package ch.ethz.ast.gdblancer.neo4j.generator;

import ch.ethz.ast.gdblancer.common.graph.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleGraphGeneratorTests {

    @Test
    void someTest() {
        Graph g = SimpleGraphGenerator.generateDenseGraph(2, 1);

        assertNotNull(g.getNodeById(0));
        assertNotNull(g.getNodeById(1));
    }

    @Test
    void assureExactAmountOfEdges() {
        Graph g = SimpleGraphGenerator.generateDenseGraph(100, 32);

        assertEquals(32, g.getM());
    }


}
