package ch.ethz.ast.gdblancer.neo4j;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.QueryReplay;

import java.io.IOException;
import java.util.List;

public class Neo4JQueryReplay extends QueryReplay {

    @Override
    protected void executeQueries(List<String> queries) {
        GlobalState<Neo4JConnection> state = new GlobalState<>();
        ExpectedErrors errors = new ExpectedErrors();
        Neo4JUtil.addFunctionErrors(errors);
        Neo4JUtil.addArithmeticErrors(errors);
        Neo4JUtil.addRegexErrors(errors);

        try (Neo4JConnection connection = new Neo4JConnection()) {
            connection.connect();
            state.setConnection(connection);

            for (String query : queries) {
                new Neo4JQuery(query, errors).execute(state);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
