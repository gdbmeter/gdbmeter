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

public class RedisGenerator implements Generator<RedisConnection> {

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
        int selectedNumber;

        switch (action) {
            case CREATE:
                selectedNumber = Randomization.nextInt(50, 70);
                break;
            case CREATE_INDEX:
                selectedNumber = Randomization.nextInt(3,  10);
                break;
            case REMOVE:
            case SET:
            case DELETE:
                selectedNumber = Randomization.nextInt(0, 8);
                break;
            case DROP_INDEX:
                selectedNumber = Randomization.nextInt(2,  5);
                break;
            default:
                throw new AssertionError(action);
        }

        return selectedNumber;
    }

    private final Schema<RedisType> schema;

    public RedisGenerator(Schema<RedisType> schema) {
        this.schema = schema;
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
            } catch (IgnoreMeException ignored) {}
        }

    }

}
