package ch.ethz.ast.gdbmeter.redis;

import ch.ethz.ast.gdbmeter.common.ExpectedErrors;
import ch.ethz.ast.gdbmeter.common.GlobalState;
import ch.ethz.ast.gdbmeter.common.QueryReplay;

import java.util.List;

public class RedisQueryReplay extends QueryReplay {

    @Override
    protected void executeQueries(List<String> queries) {
        GlobalState<RedisConnection> state = new GlobalState<>();
        ExpectedErrors errors = new ExpectedErrors();

        errors.addRegex("ERR Unable to drop index on (.*) no such index.");
        RedisUtil.addFunctionErrors(errors);
        RedisUtil.addArithmeticErrors(errors);

        try (RedisConnection connection = new RedisConnection()) {
            connection.connect();
            state.setConnection(connection);

            for (String query : queries) {
                new RedisQuery(query, errors).execute(state);
            }
        }
    }

}
