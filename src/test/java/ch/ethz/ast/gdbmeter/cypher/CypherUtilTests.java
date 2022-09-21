package ch.ethz.ast.gdbmeter.cypher;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CypherUtilTests {

    @Test
    void testGenerateValidName() {
        String name = CypherUtil.generateValidName();

        assertNotNull(name);
        assertFalse(name.isEmpty());

        // [a-zA-Z0-9]
        char firstChar = name.charAt(0);
        assertTrue((firstChar >= 48 && firstChar <= 57)
                || (firstChar >= 65 && firstChar <= 90)
                || (firstChar >= 97 && firstChar <= 122));
    }

    @Test
    void testEscape() {
        assertEquals("\"normal text\"", CypherUtil.escape("normal text"));
        assertEquals("\"te'st\"", CypherUtil.escape("te'st"));
        assertEquals("\"te\\\"st\"", CypherUtil.escape("te\"st"));
        assertEquals("\"te\\\\st\"", CypherUtil.escape("te\\st"));
    }

}
