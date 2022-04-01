package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.neo4j.Neo4JConnection;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.*;
import java.util.function.Supplier;

public class Neo4JGraphGenerator {

    enum Action {

        CREATE(Neo4JCreateGenerator::createEntities),
        CREATE_INDEX(Neo4JCreateIndexGenerator::createIndex);

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
            case CREATE_INDEX:
                selectedNumber = Randomization.nextInt(0,  5);
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

    /**
     * A valid name begins with an alphabetic character and not with a number
     * Furthermore, a valid name does not contain symbols except for underscores
     */
    static String generateValidName() {
        return Randomization.getCharacterFromAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")
                + Randomization.getStringOfAlphabet("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_");
    }

}
