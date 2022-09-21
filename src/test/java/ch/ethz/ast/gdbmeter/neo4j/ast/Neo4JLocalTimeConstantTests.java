package ch.ethz.ast.gdbmeter.neo4j.ast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Neo4JLocalTimeConstantTests {

    @Test
    void testLocalTimeConstants() {
        assertEquals(new Neo4JLocalTimeConstant(100, null, null, null, null, null).getTextRepresentation(),
                "time('100')");
        assertEquals(new Neo4JLocalTimeConstant(100, ".", 10, null, null, null).getTextRepresentation(),
                "time('100.10')");
        assertEquals(new Neo4JLocalTimeConstant(100, ".", 10, 20, null, null).getTextRepresentation(),
                "time('100.10.20')");
        assertEquals(new Neo4JLocalTimeConstant(100, ".", 10, 20, ",", 42).getTextRepresentation(),
                "time('100.10.20,42')");
    }

}
