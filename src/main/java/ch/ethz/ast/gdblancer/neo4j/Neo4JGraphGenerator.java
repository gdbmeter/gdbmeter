package ch.ethz.ast.gdblancer.neo4j;

import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.*;
import java.util.function.Supplier;

public class Neo4JGraphGenerator {

    enum Action {

        CREATE(Neo4JCreateGenerator::createEntities);

        private Supplier<String> generator;

        Action(Supplier<String> generator) {
            this.generator = generator;
        }
    }
    
    private static int mapAction(Action action) {
        int selectedNumber = 0;

        switch (action) {
            case CREATE:
                selectedNumber = Randomization.nextInt(0, 100);
        };

        return selectedNumber;
    }

    public void generate(Neo4JConnection connection) {
        List<String> queries = new ArrayList<>();

        // Sample the actions
        for (Action action : Action.values()) {
            int amount = mapAction(action);

            for (int i = 0; i < amount; i++) {
                queries.add(action.generator.get());
            }
        }

        Randomization.shuffleList(queries);

        for (String query : queries) {
            connection.executeQuery(query);
        }
    }

}
