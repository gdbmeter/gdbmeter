package ch.ethz.ast.gdbmeter.neo4j.ast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Neo4JPointConstantTests {

    @Test
    void testPointConstants() {
        assertEquals(new Neo4JPointConstant(1D, 2D).getTextRepresentation(),
                "point({ x: 1.000000, y: 2.000000 })");
        assertEquals(new Neo4JPointConstant(1D, 2D, 3D).getTextRepresentation(),
                "point({ x: 1.000000, y: 2.000000, z: 3.000000 })");
    }

}
