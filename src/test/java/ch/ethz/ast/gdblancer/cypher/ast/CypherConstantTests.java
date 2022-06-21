package ch.ethz.ast.gdblancer.cypher.ast;

import ch.ethz.ast.gdblancer.cypher.ast.CypherConstant.*;
import org.junit.jupiter.api.Test;

import static ch.ethz.ast.gdblancer.cypher.ast.CypherConstant.IntegerOctalConstant.OctalPrefix.ZERO;
import static ch.ethz.ast.gdblancer.cypher.ast.CypherConstant.IntegerOctalConstant.OctalPrefix.ZERO_O;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CypherConstantTests {

    @Test
    void testBooleanConstants() {
        assertEquals(new BooleanConstant(true).getTextRepresentation(), "true");
        assertEquals(new BooleanConstant(false).getTextRepresentation(), "false");
    }

    @Test
    void testNulLConstant() {
        assertEquals(new NullConstant().getTextRepresentation(), "null");
    }

    @Test
    void testIntegerHexConstants() {
        assertEquals(new IntegerHexConstant(300L).getTextRepresentation(), "0x12c");
        assertEquals(new IntegerHexConstant(-300L).getTextRepresentation(), "-0x12c");
    }

    @Test
    void testIntegerOctalConstants() {
        assertEquals(new IntegerOctalConstant(100L, ZERO).getTextRepresentation(), "0144");
        assertEquals(new IntegerOctalConstant(100L, ZERO_O).getTextRepresentation(), "0o144");
        assertEquals(new IntegerOctalConstant(-100L, ZERO).getTextRepresentation(), "-0144");
        assertEquals(new IntegerOctalConstant(-100L, ZERO_O).getTextRepresentation(), "-0o144");
    }

    @Test
    void testStringConstants() {
        assertEquals(new StringConstant("").getTextRepresentation(), "\"\"");
        assertEquals(new StringConstant("test").getTextRepresentation(), "\"test\"");
        assertEquals(new StringConstant("\\").getTextRepresentation(), "\"\\\\\"");
        assertEquals(new StringConstant("\"").getTextRepresentation(), "\"\\\"\"");
    }

    @Test
    void testLocalTimeConstants() {
        assertEquals(new LocalTimeConstant(100, null, null, null, null, null).getTextRepresentation(),
                "time('100')");
        assertEquals(new LocalTimeConstant(100, ".", 10, null, null, null).getTextRepresentation(),
                "time('100.10')");
        assertEquals(new LocalTimeConstant(100, ".", 10, 20, null, null).getTextRepresentation(),
                "time('100.10.20')");
        assertEquals(new LocalTimeConstant(100, ".", 10, 20, ",", 42).getTextRepresentation(),
                "time('100.10.20,42')");
    }

}
