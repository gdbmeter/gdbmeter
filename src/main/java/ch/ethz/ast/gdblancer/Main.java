package ch.ethz.ast.gdblancer;

import ch.ethz.ast.gdblancer.common.*;
import ch.ethz.ast.gdblancer.common.schema.CypherSchema;
import ch.ethz.ast.gdblancer.neo4j.Neo4JConnection;
import ch.ethz.ast.gdblancer.neo4j.Neo4JGenerator;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQueryReplay;
import ch.ethz.ast.gdblancer.neo4j.oracle.Neo4JPartitionOracle;
import ch.ethz.ast.gdblancer.redis.RedisConnection;
import ch.ethz.ast.gdblancer.redis.RedisGenerator;
import ch.ethz.ast.gdblancer.redis.RedisQueryReplay;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;
import ch.ethz.ast.gdblancer.redis.oracle.RedisEmptyResultOracle;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import org.neo4j.cypher.internal.parser.javacc.Cypher;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;

public class Main {

    private enum Database {
        NEO4J,
        REDIS_GRAPH
    }

    private static Database systemUnderTest;

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Use 0/1 as the first parameter");
            System.exit(0);
        }

        int option = Integer.parseInt(args[0]);

        switch (option) {
            case 0:
                systemUnderTest = Database.NEO4J;
                break;
            case 1:
                systemUnderTest = Database.REDIS_GRAPH;
                break;
            default:
                System.out.println("Unknown option, use 0 or 1");
                System.exit(0);
        }

        runOracle();
    }

    // TODO: Make rest generic
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
                Main.<Neo4JConnection>run(Neo4JConnection.class, new GlobalState<>(), Neo4JPartitionOracle.class, Neo4JGenerator.class);
                break;
            case REDIS_GRAPH:
                runRedisOracle();
                break;
            default:
                throw new AssertionError(systemUnderTest);
        }
    }

    private static <C extends Connection> void run(Class<C> connectionClass,
                                                    GlobalState<C> state,
                                                    Class<Oracle> oracleClass,
                                                    Class<Generator<C>> generatorClass) throws Exception {
        while (true) {
            try (C connection = connectionClass.getDeclaredConstructor().newInstance()) {
                connection.connect();
                state.setConnection(connection);

                CypherSchema schema = CypherSchema.generateRandomSchema();

                Oracle oracle = oracleClass.getDeclaredConstructor(state.getClass(), CypherSchema.class).newInstance(state, schema);
                oracle.onGenerate();

                generatorClass.getDeclaredConstructor(CypherSchema.class).newInstance(schema).generate(state);
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

    private static void runNeo4JOracle() throws IOException {
        GlobalState<Connection> state = new GlobalState<>();

        while (true) {
            try (Connection connection = new Neo4JConnection()) {
                connection.connect();
                state.setConnection(connection);

                CypherSchema schema = CypherSchema.generateRandomSchema();
                Oracle oracle = new Neo4JPartitionOracle(state, schema);
                oracle.onGenerate();

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
