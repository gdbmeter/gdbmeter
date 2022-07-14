package ch.ethz.ast.gdblancer.janus;

import ch.ethz.ast.gdblancer.common.Generator;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.schema.Entity;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.janus.gen.*;
import ch.ethz.ast.gdblancer.janus.query.JanusQueryAdapter;
import ch.ethz.ast.gdblancer.janus.schema.JanusType;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;
import org.janusgraph.core.Cardinality;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.schema.JanusGraphManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class JanusGenerator implements Generator<JanusConnection> {

    enum Action {
        CREATE(JanusCreateGenerator::createEntities),
        CREATE_INDEX(JanusCreateIndexGenerator::createIndex),
        DROP_INDEX(JanusRemoveIndexGenerator::dropIndex),
        DELETE(JanusDeleteGenerator::deleteNodes);
        // SET(JanusPropertyUpdateGenerator::updateProperties);
        // REMOVE(Neo4JRemoveGenerator::removeProperties);

        private final Function<Schema<JanusType>, JanusQueryAdapter> generator;

        Action(Function<Schema<JanusType>, JanusQueryAdapter> generator) {
            this.generator = generator;
        }
    }

    private static int mapAction(Action action) {
        int selectedNumber;

        switch (action) {
            case CREATE:
                selectedNumber = Randomization.nextInt(20, 30);
                break;
            case DELETE:
                selectedNumber = Randomization.nextInt(5, 10);
                break;
            case CREATE_INDEX:
            case DROP_INDEX:
                selectedNumber = Randomization.nextInt(0,  3);
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

    public void createJanusSchema(JanusGraph graph) {
        JanusGraphManagement management = graph.openManagement();

        for (String label : schema.getLabels()) {
            management.makeVertexLabel(label).make();

            Entity<JanusType> entity = schema.getEntityByLabel(label);

            for (Map.Entry<String, JanusType> property : entity.getAvailableProperties().entrySet()) {
                management.makePropertyKey(property.getKey())
                        .cardinality(Cardinality.SINGLE)
                        .dataType(property.getValue().getJavaClass()).make();
            }
        }

        for (String label : schema.getTypes()) {
            management.makeEdgeLabel(label).make();

            Entity<JanusType> entity = schema.getEntityByType(label);

            for (Map.Entry<String, JanusType> property : entity.getAvailableProperties().entrySet()) {
                management.makePropertyKey(property.getKey())
                        .cardinality(Cardinality.SINGLE)
                        .dataType(property.getValue().getJavaClass()).make();
            }
        }

        management.commit();
    }

    public void generate(GlobalState<JanusConnection> globalState) {
        createJanusSchema(globalState.getConnection().getGraph());
        List<Function<Schema<JanusType>, JanusQueryAdapter>> queries = new ArrayList<>();

        // Sample the actions
        for (Action action : Action.values()) {
            int amount = mapAction(action);

            for (int i = 0; i < amount; i++) {
                queries.add(action.generator);
            }
        }

        Randomization.shuffleList(queries);

        for (Function<Schema<JanusType>, JanusQueryAdapter> queryGenerator : queries) {
            try {
                int tries = 0;
                boolean success;
                JanusQueryAdapter query;

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
