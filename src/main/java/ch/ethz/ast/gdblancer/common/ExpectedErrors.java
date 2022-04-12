package ch.ethz.ast.gdblancer.common;

import java.util.HashSet;
import java.util.Set;

public class ExpectedErrors {

    private final Set<String> errors = new HashSet<>();

    public void add(String error) {
        errors.add(error);
    }

    public boolean isExpected(Exception exception) {
        String message = exception.getMessage();

        for (String error : errors) {
            if (message.contains(error)) {
                return true;
            }
        }

        return false;
    }

    public static ExpectedErrors from(String... errors) {
        ExpectedErrors expectedErrors = new ExpectedErrors();

        for (String error : errors) {
            expectedErrors.add(error);
        }

        return expectedErrors;
    }

}
