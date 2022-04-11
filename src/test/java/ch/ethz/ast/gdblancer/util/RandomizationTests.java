package ch.ethz.ast.gdblancer.util;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class RandomizationTests {

    @Test
    public void testA() {
        assertFalse(Randomization.nonEmptySubset(Set.of(1, 2, 3, 4)).isEmpty());
    }

}
