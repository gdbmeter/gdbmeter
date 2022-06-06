package ch.ethz.ast.gdblancer.redis;

import ch.ethz.ast.gdblancer.common.Connection;
import ch.ethz.ast.gdblancer.common.Query;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.graph.ResultSet;

import java.io.IOException;
import java.util.*;

public class RedisConnection implements Connection {

    private JedisPool pool;

    @Override
    public void connect() throws IOException {
        pool = new JedisPool("localhost", 6379);

        try (Jedis resource = pool.getResource()) {
            try (Transaction transaction = new Transaction(resource)) {
                transaction.graphDelete("db");
                transaction.exec();
            }
        }
    }

    public List<Map<String, Object>> execute(Query<RedisConnection> query) {
        List<Map<String, Object>> resultRows = null;

        try (Jedis resource = pool.getResource()) {
            try (Transaction transaction = new Transaction(resource)) {
                Response<ResultSet> response = transaction.graphQuery("db", query.getQuery());
                transaction.exec();
                response.get();
            }
        }

        return resultRows;
    }

    @Override
    public void close()  {
        pool.close();
    }

    // RedisGraph does not support names indices
    public Set<String> getIndexNames() {
        return Collections.emptySet();
    }

}
