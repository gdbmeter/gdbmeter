package ch.ethz.ast.gdbmeter;

import ch.ethz.ast.gdbmeter.common.*;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.janus.JanusProvider;
import ch.ethz.ast.gdbmeter.neo4j.Neo4JProvider;
import ch.ethz.ast.gdbmeter.redis.RedisProvider;
import ch.ethz.ast.gdbmeter.util.IgnoreMeException;
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
                .programName("GDBMeter")
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

        System.out.printf("   _____ _____  ____  __  __      _            \n" +
                        "  / ____|  __ \\|  _ \\|  \\/  |    | |           \n" +
                        " | |  __| |  | | |_) | \\  / | ___| |_ ___ _ __ \n" +
                        " | | |_ | |  | |  _ <| |\\/| |/ _ \\ __/ _ \\ '__|\n" +
                        " | |__| | |__| | |_) | |  | |  __/ ||  __/ |   \n" +
                        "  \\_____|_____/|____/|_|  |_|\\___|\\__\\___|_|   \n" +
                        "                                               \n" +
                        "                                               " +
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
