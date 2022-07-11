package ch.ethz.ast.gdblancer.janus;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.GlobalState;

import java.util.List;
import java.util.Map;

public class JanusQuery extends JanusQueryAdapter {

    private final String query;

    public JanusQuery(String query) {
        this(query, new ExpectedErrors());
    }

    public JanusQuery(String query, boolean couldAffectSchema) {
        this(query, new ExpectedErrors(), couldAffectSchema);
    }

    public JanusQuery(String query, ExpectedErrors expectedErrors) {
        this(query, expectedErrors, false);
    }

    public JanusQuery(String query, ExpectedErrors expectedErrors, boolean couldAffectSchema) {
        super(expectedErrors, couldAffectSchema);

        this.query = query;
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public boolean execute(GlobalState<JanusConnection> globalState) {
        JanusConnection connection = globalState.getConnection();
        globalState.getLogger().info(getQuery());

        try {
            connection.execute(this);
            return true;
        } catch (Exception exception) {
            checkException(exception);
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> executeAndGet(GlobalState<JanusConnection> globalState) {
        JanusConnection connection = globalState.getConnection();
        globalState.getLogger().info(getQuery());

        try {
            return connection.execute(this);
        } catch (Exception exception) {
            checkException(exception);
        }

        return null;
    }

    private void checkException(Exception e) throws AssertionError {
        if (!getExpectedErrors().isExpected(e)) {
            throw new AssertionError(getQuery(), e);
        }
    }

}
