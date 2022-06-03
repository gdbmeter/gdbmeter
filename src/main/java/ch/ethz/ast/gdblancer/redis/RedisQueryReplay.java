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

        try (RedisConnection connection = new RedisConnection()) {
            connection.connect();
            state.setConnection(connection);

            for (String query : queries) {
                new RedisQuery(query, new ExpectedErrors()).execute(state);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
