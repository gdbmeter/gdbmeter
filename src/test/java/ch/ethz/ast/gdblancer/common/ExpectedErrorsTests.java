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

    @Test
    void testExpectedRegexErrorIsExpected() {
        ExpectedErrors errors = new ExpectedErrors();
        errors.addRegex("integer, \\d+, is too large");

        assertTrue(errors.isExpected(new Exception("integer, 26684475740775100355, is too large")));
    }

    @Test
    void testNullExceptionMessageIsUnexpected() {
        ExpectedErrors errors = new ExpectedErrors();
        assertFalse(errors.isExpected(new Exception()));
    }

}
