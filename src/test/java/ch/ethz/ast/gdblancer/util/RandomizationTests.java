package ch.ethz.ast.gdblancer.util;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RandomizationTests {

    @Test
    public void testNonEmptySubsetReturnsNonEmptySet() {
        assertFalse(Randomization.nonEmptySubset(Set.of(1, 2, 3, 4)).isEmpty());
    }

    @Test
    public void testGenerateUniqueElement() {
        assertEquals(2, Randomization.generateUniqueElement(Set.of(1), () -> Randomization.getBoolean() ? 2 : 1));
    }

}
