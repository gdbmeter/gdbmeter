package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.neo4j.Neo4JConnection;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Neo4JGraphGenerator {

    enum Action {
        CREATE(Neo4JCreateGenerator::createEntities),
        CREATE_INDEX(Neo4JCreateIndexGenerator::createIndex),
        DROP_INDEX(Neo4JDropIndexGenerator::dropIndex);

        private final Function<Neo4JDBSchema, String> generator;

        Action(Function<Neo4JDBSchema, String> generator) {
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
        };

        return selectedNumber;
    }

    public void generate(Neo4JConnection connection) {
        Neo4JDBSchema schema = Neo4JDBSchema.generateRandomSchema();
        List<Function<Neo4JDBSchema, String>> queries = new ArrayList<>();

        // Sample the actions
        for (Action action : Action.values()) {
            int amount = mapAction(action);

            for (int i = 0; i < amount; i++) {
                queries.add(action.generator);
            }
        }

        Randomization.shuffleList(queries);

        for (Function<Neo4JDBSchema, String> query : queries) {
            connection.executeQuery(query.apply(schema));
        }
    }

}
