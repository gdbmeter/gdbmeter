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

        errors.add("There already exists an index");
        errors.add("An equivalent index already exists");

        try (Neo4JConnection connection = new Neo4JConnection()) {
            connection.connect();
            state.setConnection(connection);

            for (String query : queries) {
                System.out.println(new Neo4JQuery(query, errors).executeAndGet(state));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
