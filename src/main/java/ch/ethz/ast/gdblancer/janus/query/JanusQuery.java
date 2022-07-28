package ch.ethz.ast.gdblancer.janus.query;

import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.StringQuery;
import ch.ethz.ast.gdblancer.janus.JanusConnection;

import java.util.List;
import java.util.Map;

public class JanusQuery extends StringQuery<JanusConnection> {

    public JanusQuery(String query) {
        super(query);
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
