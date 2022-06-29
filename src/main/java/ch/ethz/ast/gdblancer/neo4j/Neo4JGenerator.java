package ch.ethz.ast.gdblancer.neo4j;

import ch.ethz.ast.gdblancer.common.Generator;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.neo4j.gen.*;
import ch.ethz.ast.gdblancer.common.schema.CypherSchema;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Neo4JGenerator implements Generator<Neo4JConnection> {

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

        private final Function<CypherSchema, Neo4JQuery> generator;

        Action(Function<CypherSchema, Neo4JQuery> generator) {
            this.generator = generator;
        }
    }
    
    private static int mapAction(Action action) {
        int selectedNumber = 0;

        switch (action) {
            case CREATE:
                selectedNumber = Randomization.nextInt(20, 30);
                break;
            case DELETE:
            case SET:
            case REMOVE:
                selectedNumber = Randomization.nextInt(0, 8);
                break;
            case CREATE_INDEX:
                selectedNumber = Randomization.nextInt(3,  10);
                break;
            case DROP_INDEX:
            case SHOW_FUNCTIONS:
            case SHOW_PROCEDURES:
            case SHOW_TRANSACTIONS:
                selectedNumber = Randomization.nextInt(2,  5);
                break;
            default:
                throw new AssertionError(action);
        }

        return selectedNumber;
    }

    private final CypherSchema schema;

    public Neo4JGenerator(CypherSchema schema) {
        this.schema = schema;
    }

    public void generate(GlobalState<Neo4JConnection> globalState) {
        List<Function<CypherSchema, Neo4JQuery>> queries = new ArrayList<>();

        // Sample the actions
        for (Action action : Action.values()) {
            int amount = mapAction(action);

            for (int i = 0; i < amount; i++) {
                queries.add(action.generator);
            }
        }

        Randomization.shuffleList(queries);

        for (Function<CypherSchema, Neo4JQuery> queryGenerator : queries) {
            try {
                int tries = 0;
                boolean success;
                Neo4JQuery query;

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
