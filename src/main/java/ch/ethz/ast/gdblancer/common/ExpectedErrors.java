package ch.ethz.ast.gdblancer.common;

import ch.ethz.ast.gdblancer.neo4j.Neo4JBugs;
import ch.ethz.ast.gdblancer.redis.RedisBugs;
import org.neo4j.graphdb.QueryExecutionException;
import org.neo4j.logging.shaded.log4j.core.pattern.AbstractStyleNameConverter;
import redis.clients.jedis.exceptions.JedisDataException;

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

        if (Neo4JBugs.bug12869 && exception instanceof IndexOutOfBoundsException) {
            return true;
        }

        if (exception instanceof QueryExecutionException) {
            if (Neo4JBugs.bug12877) {
                if (((QueryExecutionException) exception).getStatusCode().equals("Neo.DatabaseError.Statement.ExecutionFailed")) {
                    if (message.startsWith("Expected \nRegularSinglePlannerQuery(QueryGraph {Nodes: ['n'],")) {
                        return true;
                    }
                }
            }

            if (Neo4JBugs.bug12878) {
                if (message.startsWith("Node with id") && message.endsWith("has been deleted in this transaction")) {
                    return true;
                }
            }
        }

        if (Neo4JBugs.bug12880) {
            if (exception instanceof IllegalArgumentException) {
                if (message.equals("Comparison method violates its general contract!")) {
                    return true;
                }
            }
        }

        if (Neo4JBugs.bug12879) {
            if (exception instanceof UnsupportedOperationException) {
                if (message.equals("TEXT index has no value capability")) {
                    return true;
                }
            }
        }

        if (Neo4JBugs.bug12881) {
            if (exception instanceof IllegalStateException) {
                if (message.startsWith("Did not find any type information for expression")) {
                    return true;
                }
            }
        }

        if (RedisBugs.bug3010) {
            if (exception instanceof NumberFormatException) {
                if (message.equals("For input string: \"inf\"") ||
                        message.equals("For input string: \"-nan\"") ||
                        message.equals("For input string: \"-inf\"")) {
                    return true;
                }
            }
        }

        if (RedisBugs.bug2383) {
            if (exception instanceof JedisDataException) {
                if (message.matches("Type mismatch: expected (Integer|Boolean|String|Float|Null) but was (Integer|Boolean|String|Float|Null)")) {
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
