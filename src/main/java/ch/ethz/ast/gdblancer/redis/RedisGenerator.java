package ch.ethz.ast.gdblancer.redis;

import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.redis.gen.RedisCreateGenerator;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RedisGenerator {

    enum Action {
        CREATE(RedisCreateGenerator::createEntities);

        private final Function<Neo4JDBSchema, RedisQuery> generator;

        Action(Function<Neo4JDBSchema, RedisQuery> generator) {
            this.generator = generator;
        }
    }

    private static int mapAction(Action action) {
        int selectedNumber = 0;

        switch (action) {
            case CREATE:
                selectedNumber = Randomization.nextInt(50, 70);
                break;
            default:
                throw new AssertionError(action);
        }

        return selectedNumber;
    }

    private final Neo4JDBSchema schema;

    public RedisGenerator(Neo4JDBSchema schema) {
        this.schema = schema;
    }

    public void generate(GlobalState<RedisConnection> globalState) {
        List<Function<Neo4JDBSchema, RedisQuery>> queries = new ArrayList<>();

        // Sample the actions
        for (Action action : Action.values()) {
            int amount = mapAction(action);

            for (int i = 0; i < amount; i++) {
                queries.add(action.generator);
            }
        }

        Randomization.shuffleList(queries);

        for (Function<Neo4JDBSchema, RedisQuery> queryGenerator : queries) {
            try {
                int tries = 0;
                boolean success;
                RedisQuery query;

                do {
                    query = queryGenerator.apply(schema);
                    success = query.execute(globalState);
                } while (!success && tries++ < 1000);

                if (success && query.couldAffectSchema()) {
                    // TODO: Move to global state later
                    schema.setIndices(globalState.getConnection().getIndexNames());
                }
            } catch (IgnoreMeException ignored) {}
        }

    }

}
