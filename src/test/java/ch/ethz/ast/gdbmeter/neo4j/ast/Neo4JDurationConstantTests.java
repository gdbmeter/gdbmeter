package ch.ethz.ast.gdbmeter.neo4j.ast;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Neo4JDurationConstantTests {

    @Test
    void testDurationConstants() {
        assertThrows(IllegalArgumentException.class, () -> new Neo4JDurationConstant(Collections.emptyMap(), Collections.emptyMap()));
        assertEquals(new Neo4JDurationConstant("test").getTextRepresentation(), "duration('test')");
        assertEquals(
                new Neo4JDurationConstant(Map.of("Y", 1L), Collections.emptyMap()).getTextRepresentation(),
                "duration('P1Y')");
        assertEquals(
                new Neo4JDurationConstant(Collections.emptyMap(), Map.of("M", 1L)).getTextRepresentation(),
                "duration('PT1M')");
        assertEquals(
                new Neo4JDurationConstant(Map.of("Y", 1L), Map.of("M", 1L)).getTextRepresentation(),
                "duration('P1YT1M')");
    }

}
