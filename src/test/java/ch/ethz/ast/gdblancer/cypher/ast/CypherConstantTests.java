package ch.ethz.ast.gdblancer.cypher.ast;

import ch.ethz.ast.gdblancer.cypher.ast.CypherConstant.*;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import org.junit.jupiter.api.Test;

import static ch.ethz.ast.gdblancer.cypher.ast.CypherConstant.IntegerOctalConstant.OctalPrefix.ZERO;
import static ch.ethz.ast.gdblancer.cypher.ast.CypherConstant.IntegerOctalConstant.OctalPrefix.ZERO_O;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void testIllegalDateConstants() {
        assertThrows(IgnoreMeException.class, () ->
                new DateConstant(true, 100, 2, 99999));
        assertThrows(IgnoreMeException.class, () ->
                new DateConstant(true, 100, 4, 31));
        assertThrows(IgnoreMeException.class, () ->
                new DateConstant(true, 2019, 2, 29));
    }

    @Test
    void testLegalDateConstants() {
        assertEquals(new DateConstant(true, 2020, 2, 28).getTextRepresentation(),
                "date('2020-02-28')");
        assertEquals(new DateConstant(false, 2020, 2, 28).getTextRepresentation(),
                "date('20200228')");
    }

    @Test
    void testPointConstants() {
        assertEquals(new PointConstant(1D, 2D).getTextRepresentation(),
                "point({ x: 1.000000, y: 2.000000 })");
        assertEquals(new PointConstant(1D, 2D, 3D).getTextRepresentation(),
                "point({ x: 1.000000, y: 2.000000, z: 3.000000 })");
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
