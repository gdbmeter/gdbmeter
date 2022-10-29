package ch.ethz.ast.gdbmeter.cypher.ast;

import ch.ethz.ast.gdbmeter.cypher.ast.CypherConstant.*;
import org.junit.jupiter.api.Test;

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
        assertEquals(new IntegerOctalConstant(100L).getTextRepresentation(), "0o144");
        assertEquals(new IntegerOctalConstant(-100L).getTextRepresentation(), "-0o144");
    }

    @Test
    void testStringConstants() {
        assertEquals(new StringConstant("").getTextRepresentation(), "\"\"");
        assertEquals(new StringConstant("test").getTextRepresentation(), "\"test\"");
        assertEquals(new StringConstant("\\").getTextRepresentation(), "\"\\\\\"");
        assertEquals(new StringConstant("\"").getTextRepresentation(), "\"\\\"\"");
    }

}
