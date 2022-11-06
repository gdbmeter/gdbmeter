package ch.ethz.ast.gdbmeter.neo4j;

import ch.ethz.ast.gdbmeter.common.Generator;
import ch.ethz.ast.gdbmeter.common.GlobalState;
import ch.ethz.ast.gdbmeter.neo4j.gen.*;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.neo4j.schema.Neo4JType;
import ch.ethz.ast.gdbmeter.util.IgnoreMeException;
import ch.ethz.ast.gdbmeter.util.Randomization;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public record Neo4JGenerator(
        Schema<Neo4JType> schema) implements Generator<Neo4JConnection> {

    enum Action {
        CREATE(Neo4JCreateGenerator::createEntities),
        CREATE_INDEX(Neo4JCreateIndexGenerator::createIndex),
        DROP_INDEX(Neo4JDropIndexGenerator::dropIndex),
        SHOW_FUNCTIONS(Neo4JShowFunctionsGenerator::showFunctions),
        SHOW_PROCEDURES(Neo4JShowProceduresGenerator::showProcedures),
        SHOW_TRANSACTIONS(Neo4JShowTransactionsGenerator::showTransactions),
        DELETE(Neo4JDeleteGenerator::deleteNodes),
        SET(Neo4JSetGenerator::setProperties),
        REMOVE(Neo4JRemoveGenerator::removeProperties);

        private final Function<Schema<Neo4JType>, Neo4JQuery> generator;

        Action(Function<Schema<Neo4JType>, Neo4JQuery> generator) {
            this.generator = generator;
        }
    }

    private static int mapAction(Action action) {
        return switch (action) {
            case CREATE -> Randomization.nextInt(20, 30);
            case DELETE, SET, REMOVE -> Randomization.nextInt(0, 8);
            case CREATE_INDEX -> Randomization.nextInt(3, 10);
            case DROP_INDEX, SHOW_FUNCTIONS, SHOW_PROCEDURES, SHOW_TRANSACTIONS -> Randomization.nextInt(2, 5);
        };
    }

    public void generate(GlobalState<Neo4JConnection> globalState) {
        List<Function<Schema<Neo4JType>, Neo4JQuery>> queries = new ArrayList<>();

        // Sample the actions
        for (Action action : Action.values()) {
            int amount = mapAction(action);

            for (int i = 0; i < amount; i++) {
                queries.add(action.generator);
            }
        }

        Randomization.shuffleList(queries);

        for (Function<Schema<Neo4JType>, Neo4JQuery> queryGenerator : queries) {
            try {
                int tries = 0;
                boolean success;
                Neo4JQuery query;

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
