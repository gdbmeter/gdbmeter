package ch.ethz.ast.gdblancer.common;

public abstract class StringQuery<C extends Connection> extends Query<C> {

    private final String query;

    public StringQuery(String query) {
        this(query, new ExpectedErrors());
    }

    public StringQuery(String query, boolean couldAffectSchema) {
        this(query, new ExpectedErrors(), couldAffectSchema);
    }

    public StringQuery(String query, ExpectedErrors expectedErrors) {
        this(query, expectedErrors, false);
    }

    public StringQuery(String query, ExpectedErrors expectedErrors, boolean couldAffectSchema) {
        super(expectedErrors, couldAffectSchema);
        this.query = query;
    }

    @Override
    public String getQuery() {
        return query;
    }

}
