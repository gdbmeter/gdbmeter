package ch.ethz.ast.gdblancer.neo4j;

import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.neo4j.gen.Neo4JCreateGenerator;
import ch.ethz.ast.gdblancer.neo4j.gen.Neo4JCreateIndexGenerator;
import ch.ethz.ast.gdblancer.neo4j.gen.Neo4JDropIndexGenerator;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Neo4JGenerator {

    enum Action {
        CREATE(Neo4JCreateGenerator::createEntities),
        CREATE_INDEX(Neo4JCreateIndexGenerator::createIndex),
        DROP_INDEX(Neo4JDropIndexGenerator::dropIndex);

        private final Function<Neo4JDBSchema, Query> generator;

        Action(Function<Neo4JDBSchema, Query> generator) {
            this.generator = generator;
        }
    }
    
    private static int mapAction(Action action) {
        int selectedNumber = 0;

        switch (action) {
            case CREATE:
                selectedNumber = Randomization.nextInt(0, 30);
                break;
            case CREATE_INDEX:
                selectedNumber = Randomization.nextInt(0,  5);
                break;
            case DROP_INDEX:
                selectedNumber = Randomization.nextInt(0,  2);
                break;
        }

        return selectedNumber;
    }

    public void generate(GlobalState<Neo4JConnection> globalState) {
        Neo4JDBSchema schema = Neo4JDBSchema.generateRandomSchema();
        List<Function<Neo4JDBSchema, Query>> queries = new ArrayList<>();

        // Sample the actions
        for (Action action : Action.values()) {
            int amount = mapAction(action);

            for (int i = 0; i < amount; i++) {
                queries.add(action.generator);
            }
        }

        Randomization.shuffleList(queries);

        for (Function<Neo4JDBSchema, Query> queryGenerator : queries) {
            try {
                int tries = 0;
                boolean success;
                Query query;

                do {
                    query = queryGenerator.apply(schema);
                    success = globalState.execute(query);
                } while (!success && tries++ < 1000);

                if (success && query.couldAffectSchema()) {
                    // TODO: Move to global state later
                    schema.setIndices(globalState.getConnection().getIndexNames());
                }
            } catch (IgnoreMeException ignored) {
                globalState.getLogger().info("Ignore me exception thrown");
            }
        }
    }

}
