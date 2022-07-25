package ch.ethz.ast.gdblancer.common;

import java.util.List;
import java.util.Map;

public abstract class Query<C extends Connection> {

    private final ExpectedErrors expectedErrors;
    private final boolean couldAffectSchema;

    public Query() {
        this(new ExpectedErrors());
    }

    public Query(boolean couldAffectSchema) {
        this(new ExpectedErrors(), couldAffectSchema);
    }

    public Query(ExpectedErrors expectedErrors) {
        this(expectedErrors, false);
    }

    public Query(ExpectedErrors expectedErrors, boolean couldAffectSchema) {
        this.expectedErrors = expectedErrors;
        this.couldAffectSchema = couldAffectSchema;
    }

    public abstract String getQuery();

    public ExpectedErrors getExpectedErrors() {
        return expectedErrors;
    }

    public boolean couldAffectSchema() {
        return couldAffectSchema;
    }

    public abstract boolean execute(GlobalState<C> globalState);

    public abstract List<Map<String, Object>> executeAndGet(GlobalState<C> globalState);

}
