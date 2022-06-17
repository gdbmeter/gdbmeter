package ch.ethz.ast.gdblancer.cypher.ast;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static ch.ethz.ast.gdblancer.cypher.ast.CypherBinaryArithmeticOperation.ArithmeticOperator.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CypherBinaryArithmeticOperationTests {

    @Test
    void testGetRandomFloatOperatorNaNSafe() {
        assertTrue(Stream.of(ADDITION, SUBTRACTION, MULTIPLICATION)
                .anyMatch(arithmeticOperator -> arithmeticOperator.equals(getRandomFloatOperatorNaNSafe())));
    }

    @Test
    void testGetRandomIntegerOperatorNaNSafe() {
        assertTrue(Stream.of(ADDITION, SUBTRACTION, MULTIPLICATION)
                .anyMatch(arithmeticOperator -> arithmeticOperator.equals(getRandomIntegerOperatorNaNSafe())));
    }

}
