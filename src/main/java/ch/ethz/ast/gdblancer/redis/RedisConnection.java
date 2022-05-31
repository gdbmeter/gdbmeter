package ch.ethz.ast.gdblancer.redis;

import ch.ethz.ast.gdblancer.common.Connection;
import ch.ethz.ast.gdblancer.common.Query;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisConnection implements Connection {

    private JedisPool pool;

    @Override
    public void connect() throws IOException {
        pool = new JedisPool("localhost", 6379);

        try (Jedis resource = pool.getResource()) {
            resource.flushAll();
        }
    }

    public List<Map<String, Object>> execute(Query<RedisConnection> query) {
        List<Map<String, Object>> resultRows = null;

        try (Jedis resource = pool.getResource()) {
            try (Transaction transaction = new Transaction(resource)) {
                transaction.graphQuery("db", query.getQuery());
                transaction.exec();
            }
        }

        return resultRows;
    }

    @Override
    public void close()  {
        pool.close();
    }

    public Set<String> getIndexNames() {
        Set<String> indices = new HashSet<>();

        // TODO: Use SHOW INDEXES

        return indices;
    }

}
