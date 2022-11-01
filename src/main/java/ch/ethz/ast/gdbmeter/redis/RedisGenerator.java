package ch.ethz.ast.gdbmeter.redis;

import ch.ethz.ast.gdbmeter.common.Generator;
import ch.ethz.ast.gdbmeter.common.GlobalState;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.redis.gen.*;
import ch.ethz.ast.gdbmeter.redis.schema.RedisType;
import ch.ethz.ast.gdbmeter.util.IgnoreMeException;
import ch.ethz.ast.gdbmeter.util.Randomization;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public record RedisGenerator(
        Schema<RedisType> schema) implements Generator<RedisConnection> {

    enum Action {
        CREATE(RedisCreateGenerator::createEntities),
        REMOVE(RedisRemoveGenerator::removeProperties),
        CREATE_INDEX(RedisCreateIndexGenerator::createIndex),
        DROP_INDEX(RedisDropIndexGenerator::dropIndex),
        SET(RedisSetGenerator::setProperties),
        DELETE(RedisDeleteGenerator::deleteNodes);

        private final Function<Schema<RedisType>, RedisQuery> generator;

        Action(Function<Schema<RedisType>, RedisQuery> generator) {
            this.generator = generator;
        }
    }

    private static int mapAction(Action action) {
        return switch (action) {
            case CREATE -> Randomization.nextInt(50, 70);
            case CREATE_INDEX -> Randomization.nextInt(3, 10);
            case REMOVE, SET, DELETE -> Randomization.nextInt(0, 8);
            case DROP_INDEX -> Randomization.nextInt(2, 5);
        };
    }

    @Override
    public void generate(GlobalState<RedisConnection> globalState) {
        List<Function<Schema<RedisType>, RedisQuery>> queries = new ArrayList<>();

        // Sample the actions
        for (Action action : Action.values()) {
            int amount = mapAction(action);

            for (int i = 0; i < amount; i++) {
                queries.add(action.generator);
            }
        }

        Randomization.shuffleList(queries);

        for (Function<Schema<RedisType>, RedisQuery> queryGenerator : queries) {
            try {
                int tries = 0;
                boolean success;
                RedisQuery query;

                do {
                    query = queryGenerator.apply(schema);
                    success = query.execute(globalState);
                } while (!success && tries++ < 1000);

                if (success && query.couldAffectSchema()) {
                    schema.setIndices(globalState.getConnection().getIndexNames());
                }
            } catch (IgnoreMeException ignored) {
            }
        }

    }

}
