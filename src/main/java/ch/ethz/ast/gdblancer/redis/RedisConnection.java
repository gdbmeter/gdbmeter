package ch.ethz.ast.gdblancer.redis;

import ch.ethz.ast.gdblancer.common.Connection;
import ch.ethz.ast.gdblancer.common.Query;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.graph.ResultSet;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisConnection implements Connection {

    private Jedis jedis;

    @Override
    public void connect() throws IOException {
        jedis = new Jedis("localhost", 6379);

        // This should prevent #2394
        try (Transaction transaction = jedis.multi()) {
            Response<String> response = transaction.graphDelete("db");
            transaction.exec();
            System.out.println(response.get());
        }
    }

    public List<Map<String, Object>> execute(Query<RedisConnection> query) {
        List<Map<String, Object>> resultRows = null;

        try (Transaction transaction = jedis.multi()) {
            Response<ResultSet> response = transaction.graphQuery("db", query.getQuery());
            transaction.exec();
            System.out.println(response.get());
        }

        return resultRows;
    }

    @Override
    public void close() {
        jedis.close();
    }

    // RedisGraph does not support names indices
    public Set<String> getIndexNames() {
        return Collections.emptySet();
    }

}
