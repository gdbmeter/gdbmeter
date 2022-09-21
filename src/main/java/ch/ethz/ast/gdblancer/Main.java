package ch.ethz.ast.gdblancer;

import ch.ethz.ast.gdblancer.common.*;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.janus.JanusProvider;
import ch.ethz.ast.gdblancer.neo4j.Neo4JProvider;
import ch.ethz.ast.gdblancer.redis.RedisProvider;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.io.IOException;
import java.nio.file.FileSystems;

public class Main {

    @Parameters(separators = "=")
    static
    class Options {

        @Parameter
        private String databaseName;

        @Parameter(names = {"--oracle", "--method", "-o"}, description = "The oracle that should be executed on the database")
        private OracleType oracleType;

        @SuppressWarnings("FieldMayBeFinal")
        @Parameter(names = {"--reproduce", "--replay", "-r"}, description = "Whether the queries under logs/replay should be ran or not")
        private boolean reproduce = false;

        @SuppressWarnings("FieldMayBeFinal")
        @Parameter(names = {"--help", "-h"}, description = "Lists all supported options", help = true)
        private boolean help = false;

    }

    public static void main(String[] args) throws Exception {
        Options options = new Options();

        JCommander jc = JCommander.newBuilder()
                .addObject(options)
                .programName("GDBLancer")
                .build();

        jc.parse(args);

        if (options.help) {
            jc.usage();
            return;
        }

        Provider<?, ?> provider;

        switch (options.databaseName.toLowerCase()) {
            case "neo4j":
                provider = new Neo4JProvider();
                break;
            case "redis":
                provider = new RedisProvider();
                break;
            case "janus":
                provider = new JanusProvider();
                break;
            default:
                System.err.println("Unknown database, please use either neo4j, redis or janus");
                System.exit(1);
                return;
        }

        System.out.printf(" _____ ______ ______  _                                     \n" +
                "|  __ \\|  _  \\| ___ \\| |                                    \n" +
                "| |  \\/| | | || |_/ /| |      __ _  _ __    ___   ___  _ __ \n" +
                "| | __ | | | || ___ \\| |     / _` || '_ \\  / __| / _ \\| '__|\n" +
                "| |_\\ \\| |/ / | |_/ /| |____| (_| || | | || (__ |  __/| |   \n" +
                " \\____/|___/  \\____/ \\_____/ \\__,_||_| |_| \\___| \\___||_|   \n" +
                "                                                            \n" +
                "                                                            \n" +
                "Version: 1.0\n" +
                "Selected Database: %s\n\n", options.databaseName);

        if (options.reproduce) {
            replayQueries(provider);
        } else {
            if (options.oracleType == null) {
                System.err.println("Select an oracle to execute");
                System.exit(1);
            }

            run(provider, options.oracleType);
        }
    }

    private static void replayQueries(Provider<?, ?> provider) throws IOException {
        provider.getQueryReplay().replayFromFile(FileSystems.getDefault().getPath("logs/replay").toFile());
    }

    private static <C extends Connection, T> void run(Provider<C, T> provider, OracleType oracleType) throws Exception {
        GlobalState<C> state = new GlobalState<>();
        OracleFactory<C, T> factory = provider.getOracleFactory();

        while (true) {
            try (C connection = provider.getConnection()) {
                connection.connect();
                state.setConnection(connection);

                Schema<T> schema = provider.getSchema();
                Oracle oracle = factory.createOracle(oracleType, state, schema);
                oracle.onGenerate();

                provider.getGenerator(schema).generate(state);

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

}
