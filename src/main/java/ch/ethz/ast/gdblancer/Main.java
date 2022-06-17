package ch.ethz.ast.gdblancer;

import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Oracle;
import ch.ethz.ast.gdblancer.common.QueryReplay;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.neo4j.Neo4JConnection;
import ch.ethz.ast.gdblancer.neo4j.Neo4JGenerator;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQueryReplay;
import ch.ethz.ast.gdblancer.neo4j.oracle.Neo4JEmptyResultOracle;
import ch.ethz.ast.gdblancer.redis.RedisConnection;
import ch.ethz.ast.gdblancer.redis.RedisGenerator;
import ch.ethz.ast.gdblancer.redis.RedisQueryReplay;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;
import ch.ethz.ast.gdblancer.redis.oracle.RedisEmptyResultOracle;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.time.Duration;
import java.time.LocalTime;

public class Main {

    private enum Database {
        NEO4J,
        REDIS_GRAPH
    }

    private static Database systemUnderTest = Database.REDIS_GRAPH;

    public static void main(String[] args) throws IOException {
        runOracle();
    }

    private static void replayQueries() throws IOException {
        QueryReplay queryReplay;

        switch (systemUnderTest) {
            case NEO4J:
                queryReplay = new Neo4JQueryReplay();
                break;
            case REDIS_GRAPH:
                queryReplay = new RedisQueryReplay();
                break;
            default:
                throw new AssertionError(systemUnderTest);
        }

        queryReplay.replayFromFile(FileSystems.getDefault().getPath("logs/replay").toFile());
    }

    private static void runOracle() throws IOException {
        switch (systemUnderTest) {
            case NEO4J:
                runNeo4JOracle();
                break;
            case REDIS_GRAPH:
                runRedisOracle();
                break;
            default:
                throw new AssertionError(systemUnderTest);
        }
    }

    private static void runNeo4JOracle() throws IOException {
        GlobalState<Neo4JConnection> state = new GlobalState<>();

        while (true) {
            try (Neo4JConnection connection = new Neo4JConnection()) {
                connection.connect();
                state.setConnection(connection);

                CypherSchema schema = CypherSchema.generateRandomSchema();
                Oracle oracle = new Neo4JEmptyResultOracle(state, schema);
                oracle.onGenerate();

                state.setStartTime();
                new Neo4JGenerator(schema).generate(state);
                state.getLogger().info("Running oracle");

                try {
                    oracle.onStart();

                    for (int i = 0; i < 100; i++) {
                        try {
                            oracle.check();
                        } catch (IgnoreMeException ignored) {}
                    }
                } finally {
                    oracle.onComplete();
                }
            } finally {
                state.getLogger().info("Finished iteration, closing database");
                long seconds = Duration.between(state.getStartTime(), LocalTime.now()).getSeconds();
                int executedQueries = state.getExecutedQueries();
                state.getLogger().info(String.format("%d queries / second", executedQueries / seconds));
            }
        }
    }

    private static void runRedisOracle() throws IOException {
        GlobalState<RedisConnection> state = new GlobalState<>();

        while (true) {
            try (RedisConnection connection = new RedisConnection()) {
                connection.connect();
                state.setConnection(connection);

                CypherSchema schema = CypherSchema.generateRandomSchema(RedisExpressionGenerator.supportedTypes);
                Oracle oracle = new RedisEmptyResultOracle(state, schema);
                oracle.onGenerate();

                new RedisGenerator(schema).generate(state);
                state.getLogger().info("Running oracle");

                try {
                    oracle.onStart();

                    for (int i = 0; i < 100; i++) {
                        try {
                            oracle.check();
                        } catch (IgnoreMeException ignored) {}
                    }
                } finally {
                    oracle.onComplete();
                }
            } finally {
                state.getLogger().info("Finished iteration, closing database");
            }
        }
    }

}
