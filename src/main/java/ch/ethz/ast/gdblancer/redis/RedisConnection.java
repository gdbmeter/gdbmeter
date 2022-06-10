package ch.ethz.ast.gdblancer.redis;

import ch.ethz.ast.gdblancer.common.Connection;
import ch.ethz.ast.gdblancer.common.Query;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.graph.Record;
import redis.clients.jedis.graph.ResultSet;

import java.io.IOException;
import java.util.*;

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
        } catch (JedisDataException exception) {
            if (!exception.getMessage().equalsIgnoreCase("ERR Invalid graph operation on empty key")) {
                throw exception;
            }
        }
    }

    public List<Map<String, Object>> execute(Query<RedisConnection> query) {
        List<Map<String, Object>> resultRows = new ArrayList<>();

        try (Transaction transaction = jedis.multi()) {
            Response<ResultSet> response = transaction.graphQuery("db", query.getQuery());
            transaction.exec();
            ResultSet result = response.get();

            for (Record record : result) {
                Map<String, Object> row = new HashMap<>();

                for (String key : record.keys()) {
                    row.put(key, record.getValue(key));
                }

                resultRows.add(row);
            }
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
