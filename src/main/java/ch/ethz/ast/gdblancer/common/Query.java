package ch.ethz.ast.gdblancer.common;

public class Query {

    private final String query;
    private final ExpectedErrors expectedErrors;

    public Query(String query) {
        this(query, new ExpectedErrors());
    }

    public Query(String query, ExpectedErrors expectedErrors) {
        this.query = query;
        this.expectedErrors = expectedErrors;
    }

    public String getQuery() {
        return query;
    }

    public ExpectedErrors getExpectedErrors() {
        return expectedErrors;
    }

}
