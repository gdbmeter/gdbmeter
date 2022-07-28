package ch.ethz.ast.gdblancer.janus.schema;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PredicateGeneratorTests {

    @Test
    public void testNonEmptySubsetReturnsNonEmptySet() {
        Predicate predicate = PredicateGenerator.generateFor(JanusType.STRING);
        assertNotNull(predicate);

        String stringRepresentation = PredicateVisitor.asString(predicate);
        assertNotNull(stringRepresentation);
        assertFalse(stringRepresentation.isEmpty());
    }

}
