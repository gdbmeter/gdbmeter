package ch.ethz.ast.gdblancer.common;

import java.util.List;
import java.util.Map;

public abstract class Query<C extends Connection> {

    private final String query;
    private final ExpectedErrors expectedErrors;
    private final boolean couldAffectSchema;

    public Query(String query) {
        this(query, new ExpectedErrors());
    }

    public Query(String query, boolean couldAffectSchema) {
        this(query, new ExpectedErrors(), couldAffectSchema);
    }

    public Query(String query, ExpectedErrors expectedErrors) {
        this(query, expectedErrors, false);
    }

    public Query(String query, ExpectedErrors expectedErrors, boolean couldAffectSchema) {
        this.query = query;
        this.expectedErrors = expectedErrors;
        this.couldAffectSchema = couldAffectSchema;
    }

    public String getQuery() {
        return query;
    }

    public ExpectedErrors getExpectedErrors() {
        return expectedErrors;
    }

    public boolean couldAffectSchema() {
        return couldAffectSchema;
    }

    public abstract boolean execute(GlobalState<C> globalState);

    public abstract List<Map<String, Object>> executeAndGet(GlobalState<C> globalState);

}
