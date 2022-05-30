package ch.ethz.ast.gdblancer;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JType;
import ch.ethz.ast.gdblancer.redis.RedisConnection;
import ch.ethz.ast.gdblancer.redis.RedisGenerator;
import ch.ethz.ast.gdblancer.redis.RedisQuery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        runOracle();
    }

    public static void replayQueries() throws IOException {
        replayFromFile(FileSystems.getDefault().getPath("logs/replay").toFile());
    }

    private static void runOracle() throws IOException {
        GlobalState<RedisConnection> state = new GlobalState<>();

        while (true) {
            try (RedisConnection connection = new RedisConnection()) {
                connection.connect();
                state.setConnection(connection);

                // TODO: Make this configurable
                Neo4JDBSchema schema = Neo4JDBSchema.generateRandomSchema(new Neo4JType[]{Neo4JType.INTEGER, Neo4JType.BOOLEAN, Neo4JType.FLOAT, Neo4JType.STRING, Neo4JType.POINT});
                new RedisGenerator(schema).generate(state);
            } finally {
                state.getLogger().info("Finished iteration, closing database");
            }
        }
    }

    public static void executeQuery(String query) {
        executeQueries(Collections.singletonList(query));
    }

    private static void executeQueries(List<String> queries) {
        GlobalState<RedisConnection> state = new GlobalState<>();
        ExpectedErrors errors = new ExpectedErrors();
        // TODO: Add logic for error handling depending on SuT
        // Neo4JDBUtil.addFunctionErrors(errors);
        // Neo4JDBUtil.addArithmeticErrors(errors);
        // Neo4JDBUtil.addRegexErrors(errors);

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

    private static void replayFromFile(File file) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }

        executeQueries(lines);
    }

}
