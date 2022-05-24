package ch.ethz.ast.gdblancer;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Oracle;
import ch.ethz.ast.gdblancer.neo4j.Neo4JConnection;
import ch.ethz.ast.gdblancer.neo4j.Neo4JGenerator;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.neo4j.gen.util.Neo4JDBUtil;
import ch.ethz.ast.gdblancer.neo4j.oracle.Neo4JRefinementOracle;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        runOracle();
    }

    public static void replayQueries() throws IOException {
        replayFromFile(FileSystems.getDefault().getPath("logs/replay").toFile());
    }

    private static void runOracle() throws IOException {
        GlobalState<Neo4JConnection> state = new GlobalState<>();

        while (true) {
            try (Neo4JConnection connection = new Neo4JConnection()) {
                connection.connect();
                state.setConnection(connection);

                Neo4JDBSchema schema = Neo4JDBSchema.generateRandomSchema();
                Oracle oracle = new Neo4JRefinementOracle(state, schema);
                oracle.onGenerate();

                new Neo4JGenerator(schema).generate(state);
                state.getLogger().info("Running oracle");

                try {
                    oracle.onStart();

                    for (int i = 0; i < 100; i++) {
                        try {
                            oracle.check();
                        } catch (IgnoreMeException ignored) {
                        }
                    }
                } finally {
                    oracle.onComplete();
                }
            } finally {
                state.getLogger().info("Finished iteration, closing database");
            }
        }
    }

    public static void executeQuery(String query) {
        executeQueries(Collections.singletonList(query));
    }

    private static void executeQueries(List<String> queries) {
        GlobalState<Neo4JConnection> state = new GlobalState<>();
        ExpectedErrors errors = new ExpectedErrors();
        Neo4JDBUtil.addFunctionErrors(errors);
        Neo4JDBUtil.addArithmeticErrors(errors);
        Neo4JDBUtil.addRegexErrors(errors);

        try (Neo4JConnection connection = new Neo4JConnection()) {
            connection.connect();
            state.setConnection(connection);

            for (String query : queries) {
                List<Map<String, Object>> result = new Neo4JQuery(query, errors).executeAndGet(state);
                System.out.println(result);
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
