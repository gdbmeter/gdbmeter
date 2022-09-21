package ch.ethz.ast.gdbmeter.janus.query;

import ch.ethz.ast.gdbmeter.common.GlobalState;
import ch.ethz.ast.gdbmeter.common.StringQuery;
import ch.ethz.ast.gdbmeter.janus.JanusConnection;

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
