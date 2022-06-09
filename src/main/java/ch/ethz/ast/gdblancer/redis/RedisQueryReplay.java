package ch.ethz.ast.gdblancer.redis;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.QueryReplay;

import java.io.IOException;
import java.util.List;

public class RedisQueryReplay extends QueryReplay {

    @Override
    protected void executeQueries(List<String> queries) {
        GlobalState<RedisConnection> state = new GlobalState<>();
        ExpectedErrors errors = new ExpectedErrors();

        errors.addRegex("ERR Unable to drop index on (.*) no such index.");

        try (RedisConnection connection = new RedisConnection()) {
            connection.connect();
            state.setConnection(connection);

            for (String query : queries) {
                new RedisQuery(query, errors).execute(state);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
