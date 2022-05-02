package ch.ethz.ast.gdblancer.common;

import org.neo4j.graphdb.QueryExecutionException;

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

        // See: #12869
        if (exception instanceof IndexOutOfBoundsException) {
            return true;
        }

        if (exception instanceof QueryExecutionException) {
            // See: #12874
            if (((QueryExecutionException) exception).getStatusCode().equals("Neo.ClientError.Statement.ArithmeticError")) {
                if (exception.getMessage() == null) {
                    return true;
                }
            }

            // See: #12876
            if (((QueryExecutionException) exception).getStatusCode().equals("Neo.DatabaseError.Statement.ExecutionFailed")) {
                if (exception.getMessage().startsWith("Expected \nRegularSinglePlannerQuery(QueryGraph {Nodes: ['n'],")) {
                    return true;
                }
            }
        }

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
