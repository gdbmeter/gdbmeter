package ch.ethz.ast.gdbmeter.cypher.ast;

import ch.ethz.ast.gdbmeter.cypher.ast.CypherBinaryArithmeticOperation.ArithmeticOperator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static ch.ethz.ast.gdbmeter.cypher.ast.CypherBinaryArithmeticOperation.ArithmeticOperator.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CypherBinaryArithmeticOperationTests {

    @Test
    void testGetRandomFloatOperatorNaNSafe() {
        ArithmeticOperator operator = getRandomFloatOperatorNaNSafe();
        assertTrue(List.of(ADDITION, SUBTRACTION, MULTIPLICATION).contains(operator));
    }

    @Test
    void testGetRandomIntegerOperatorNaNSafe() {
        ArithmeticOperator operator = getRandomIntegerOperatorNaNSafe();
        assertTrue(List.of(ADDITION, SUBTRACTION, MULTIPLICATION).contains(operator));
    }

}
