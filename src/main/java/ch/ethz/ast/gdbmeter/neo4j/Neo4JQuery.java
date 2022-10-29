package ch.ethz.ast.gdbmeter.neo4j;

import ch.ethz.ast.gdbmeter.common.ExpectedErrors;
import ch.ethz.ast.gdbmeter.common.GlobalState;
import ch.ethz.ast.gdbmeter.common.StringQuery;

import java.util.List;
import java.util.Map;

public class Neo4JQuery extends StringQuery<Neo4JConnection> {

    public Neo4JQuery(String query) {
        super(query);
    }

    public Neo4JQuery(String query, boolean couldAffectSchema) {
        super(query, couldAffectSchema);
    }

    public Neo4JQuery(String query, ExpectedErrors expectedErrors) {
        super(query, expectedErrors);
    }

    public Neo4JQuery(String query, ExpectedErrors expectedErrors, boolean couldAffectSchema) {
        super(query, expectedErrors, couldAffectSchema);
    }

    @Override
    public boolean execute(GlobalState<Neo4JConnection> globalState) {
        Neo4JConnection connection = globalState.getConnection();
        globalState.appendToLog(getQuery());

        try {
            connection.execute(this);
            return true;
        } catch (Exception exception) {
            checkException(exception);
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> executeAndGet(GlobalState<Neo4JConnection> globalState) {
        Neo4JConnection connection = globalState.getConnection();
        globalState.appendToLog(getQuery());

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
