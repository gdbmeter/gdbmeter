package ch.ethz.ast.gdblancer.janus;

import ch.ethz.ast.gdblancer.common.Generator;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.neo4j.Neo4JConnection;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.neo4j.gen.*;
import ch.ethz.ast.gdblancer.neo4j.schema.Neo4JType;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class JanusGenerator implements Generator<JanusConnection> {

    enum Action {
        CREATE(JanusCreateGenerator::createEntities);

        private final Function<Schema<JanusType>, JanusQuery> generator;

        Action(Function<Schema<JanusType>, JanusQuery> generator) {
            this.generator = generator;
        }
    }

    private static int mapAction(Action action) {
        int selectedNumber;

        switch (action) {
            case CREATE:
                selectedNumber = Randomization.nextInt(20, 30);
                break;
            default:
                throw new AssertionError(action);
        }

        return selectedNumber;
    }

    private final Schema<JanusType> schema;

    public JanusGenerator(Schema<JanusType> schema) {
        this.schema = schema;
    }

    public void generate(GlobalState<JanusConnection> globalState) {
        List<Function<Schema<JanusType>, JanusQuery>> queries = new ArrayList<>();

        // Sample the actions
        for (Action action : Action.values()) {
            int amount = mapAction(action);

            for (int i = 0; i < amount; i++) {
                queries.add(action.generator);
            }
        }

        Randomization.shuffleList(queries);

        for (Function<Schema<JanusType>, JanusQuery> queryGenerator : queries) {
            try {
                int tries = 0;
                boolean success;
                JanusQuery query;

                do {
                    query = queryGenerator.apply(schema);
                    success = query.execute(globalState);
                } while (!success && tries++ < 1000);
            } catch (IgnoreMeException ignored) {}
        }

    }

}
