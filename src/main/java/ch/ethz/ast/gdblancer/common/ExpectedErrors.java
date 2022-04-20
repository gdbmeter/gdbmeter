package ch.ethz.ast.gdblancer.common;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class ExpectedErrors {

    private final Set<String> errors = new HashSet<>();
    private final Set<String> regexErrors = new HashSet<>();

    public void add(String error) {
        errors.add(error);
    }

    public void addRegex(String error) {
        regexErrors.add(error);
    }

    public boolean isExpected(Exception exception) {
        String message = exception.getMessage();

        if (message == null) {
            return false;
        }

        for (String error : errors) {
            if (message.contains(error)) {
                return true;
            }
        }

        for (String error : regexErrors) {
            if (Pattern.matches(error, message)) {
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
