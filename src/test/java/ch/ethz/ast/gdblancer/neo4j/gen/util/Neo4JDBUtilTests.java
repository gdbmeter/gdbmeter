package ch.ethz.ast.gdblancer.neo4j.gen.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Neo4JDBUtilTests {

    @Test
    void testGenerateValidName() {
        String name = Neo4JDBUtil.generateValidName();

        assertNotNull(name);
        assertFalse(name.isEmpty());

        // [a-zA-Z0-9]
        char firstChar = name.charAt(0);
        assertTrue((firstChar >= 48 && firstChar <= 57)
                || (firstChar >= 65 && firstChar <= 90)
                || (firstChar >= 97 && firstChar <= 122));
    }

}
