package ch.ethz.ast.gdblancer;

import ch.ethz.ast.gdblancer.common.*;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.janus.JanusProvider;
import ch.ethz.ast.gdblancer.neo4j.Neo4JProvider;
import ch.ethz.ast.gdblancer.redis.RedisProvider;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;

import java.io.IOException;
import java.nio.file.FileSystems;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Use 0/1/2 as the first parameter");
            System.exit(0);
        }

        int option = Integer.parseInt(args[0]);
        Provider<?, ?> provider;
        OracleType type = OracleType.NON_EMPTY_RESULT;

        switch (option) {
            case 0:
                provider = new Neo4JProvider();
                break;
            case 1:
                provider = new RedisProvider();
                break;
            case 2:
                provider = new JanusProvider();
                break;
            default:
                System.out.println("Unknown option, use 0 or 1");
                System.exit(0);
                return;
        }

        run(provider, type);
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
