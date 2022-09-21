package ch.ethz.ast.gdbmeter.neo4j.ast;

import ch.ethz.ast.gdbmeter.util.IgnoreMeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Neo4JDateConstantTests {

    @Test
    void testIllegalDateConstants() {
        assertThrows(IgnoreMeException.class, () ->
                new Neo4JDateConstant(true, 100, 2, 99999));
        assertThrows(IgnoreMeException.class, () ->
                new Neo4JDateConstant(true, 100, 4, 31));
        assertThrows(IgnoreMeException.class, () ->
                new Neo4JDateConstant(true, 2019, 2, 29));
    }

    @Test
    void testLegalDateConstants() {
        assertEquals(new Neo4JDateConstant(true, 2020, 2, 28).getTextRepresentation(),
                "date('2020-02-28')");
        assertEquals(new Neo4JDateConstant(false, 2020, 2, 28).getTextRepresentation(),
                "date('20200228')");
    }

}
