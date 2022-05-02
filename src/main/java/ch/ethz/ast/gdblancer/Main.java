package ch.ethz.ast.gdblancer;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.neo4j.Neo4JConnection;
import ch.ethz.ast.gdblancer.neo4j.Neo4JGenerator;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.neo4j.gen.util.Neo4JDBUtil;
import ch.ethz.ast.gdblancer.neo4j.oracle.Neo4JPartitionOracle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        GlobalState<Neo4JConnection> state = new GlobalState<>();

        while (true) {
            try (Neo4JConnection connection = new Neo4JConnection()) {
                connection.connect();
                state.setConnection(connection);
                Neo4JDBSchema schema = new Neo4JGenerator().generate(state);

                Neo4JPartitionOracle oracle = new Neo4JPartitionOracle(state, schema);
                for (int i = 0; i < 1000; i++) {
                    oracle.check();
                }

            } finally {
                state.getLogger().info("Finished iteration, closing database");
            }
        }
    }

    public static void executeQuery(String query) {
        executeQueries(Collections.singletonList(query));
    }

    public static void executeQueries(List<String> queries) {
        GlobalState<Neo4JConnection> state = new GlobalState<>();
        ExpectedErrors errors = new ExpectedErrors();
        Neo4JDBUtil.addFunctionErrors(errors);

        try (Neo4JConnection connection = new Neo4JConnection()) {
            connection.connect();
            state.setConnection(connection);

            for (String query : queries) {
                new Neo4JQuery(query, errors).execute(state);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void replayFromFile(File file) throws IOException {
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
