package ch.ethz.ast.gdbmeter.janus;

import ch.ethz.ast.gdbmeter.common.Generator;
import ch.ethz.ast.gdbmeter.common.GlobalState;
import ch.ethz.ast.gdbmeter.common.Query;
import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.janus.gen.*;
import ch.ethz.ast.gdbmeter.janus.schema.JanusType;
import ch.ethz.ast.gdbmeter.util.IgnoreMeException;
import ch.ethz.ast.gdbmeter.util.Randomization;
import org.janusgraph.core.Cardinality;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.schema.JanusGraphManagement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class JanusGenerator implements Generator<JanusConnection> {

    enum Action {
        CREATE(JanusCreateGenerator::createEntities),
        CREATE_INDEX(JanusCreateIndexGenerator::createIndex),
        DROP_INDEX(JanusRemoveIndexGenerator::dropIndex),
        DELETE(JanusDeleteGenerator::deleteNodes),
        SET(JanusPropertyUpdateGenerator::updateProperties),
        REMOVE(JanusPropertyRemoveGenerator::removeProperties);

        private final Function<Schema<JanusType>, Query<JanusConnection>> generator;

        Action(Function<Schema<JanusType>, Query<JanusConnection>> generator) {
            this.generator = generator;
        }
    }

    private static int mapAction(Action action) {
        int selectedNumber;

        switch (action) {
            case CREATE:
                selectedNumber = Randomization.nextInt(20, 30);
                break;
            case SET:
            case REMOVE:
                selectedNumber = Randomization.nextInt(5, 15);
                break;
            case DELETE:
                selectedNumber = Randomization.nextInt(5, 10);
                break;
            case CREATE_INDEX:
            case DROP_INDEX:
                selectedNumber = Randomization.nextInt(0, 4);
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

    public void createJanusSchema(GlobalState<JanusConnection> globalState) {
        JanusGraph graph = globalState.getConnection().getGraph();

        JanusGraphManagement management = graph.openManagement();
        List<String> logBacklog = new LinkedList<>();

        for (String label : schema.getLabels()) {
            logBacklog.add(String.format("VL:%s", label));
            management.makeVertexLabel(label).make();

            createPropertyKeys(schema.getEntityByLabel(label), management, logBacklog);
        }

        for (String label : schema.getTypes()) {
            logBacklog.add(String.format("EL:%s", label));
            management.makeEdgeLabel(label).make();

            createPropertyKeys(schema.getEntityByType(label), management, logBacklog);
        }

        globalState.appendToLog(String.format("[Schema %s]", String.join(" ", logBacklog)));
        management.commit();
    }

    public void createPropertyKeys(Entity<JanusType> entity, JanusGraphManagement management, List<String> logBacklog) {
        for (Map.Entry<String, JanusType> property : entity.getAvailableProperties().entrySet()) {
            String key = property.getKey();
            Class<?> clazz = property.getValue().getJavaClass();

            logBacklog.add(String.format("PK:%s:%s", key, clazz.getCanonicalName()));
            management.makePropertyKey(key)
                    .cardinality(Cardinality.SINGLE)
                    .dataType(clazz).make();
        }
    }

    public void generate(GlobalState<JanusConnection> globalState) {
        createJanusSchema(globalState);
        List<Function<Schema<JanusType>, Query<JanusConnection>>> queries = new ArrayList<>();

        // Sample the actions
        for (Action action : Action.values()) {
            int amount = mapAction(action);

            for (int i = 0; i < amount; i++) {
                queries.add(action.generator);
            }
        }

        Randomization.shuffleList(queries);

        for (Function<Schema<JanusType>, Query<JanusConnection>> queryGenerator : queries) {
            try {
                int tries = 0;
                boolean success;
                Query<JanusConnection> query;

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
