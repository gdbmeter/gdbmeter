package ch.ethz.ast.gdblancer.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpectedErrorsTests {

    @Test
    void testExpectedErrorIsExpected() {
        ExpectedErrors errors = new ExpectedErrors();
        errors.add("expected");

        assertTrue(errors.isExpected(new Exception("I am expected")));
    }

    @Test
    void testExpectedErrorIsExpectedInMultiple() {
        ExpectedErrors errors = new ExpectedErrors();
        errors.add("just");
        errors.add("some");
        errors.add("text");
        errors.add("expected");

        assertTrue(errors.isExpected(new Exception("I am expected")));
    }

    @Test
    void testUnexpectedErrorIsUnexpected() {
        ExpectedErrors errors = new ExpectedErrors();
        errors.add("expected");

        assertFalse(errors.isExpected(new Exception("I am groot")));
    }

    @Test
    void testUnexpectedErrorIsUnexpectedInMultiple() {
        ExpectedErrors errors = new ExpectedErrors();
        errors.add("expected");
        errors.add("just");
        errors.add("some");
        errors.add("text");

        assertFalse(errors.isExpected(new Exception("I am groot")));
    }

}
