package ch.ethz.ast.gdblancer.janus;

import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.QueryReplay;
import ch.ethz.ast.gdblancer.janus.query.JanusCreateIndexQuery;
import ch.ethz.ast.gdblancer.janus.query.JanusQuery;
import ch.ethz.ast.gdblancer.janus.query.JanusRemoveIndexQuery;
import org.janusgraph.core.schema.JanusGraphManagement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JanusQueryReplay extends QueryReplay {

    @Override
    protected void executeQueries(List<String> queries) {
        GlobalState<JanusConnection> state = new GlobalState<>();

        try (JanusConnection connection = new JanusConnection()) {
            connection.connect();
            state.setConnection(connection);

            for (String query : queries) {
                if (query.startsWith("[Schema")) {
                    JanusGraphManagement m = connection.getGraph().openManagement();

                    query = query.substring(0, query.length() - 1);
                    String[] parts = query.split(" ");

                    // We start at index 1 since the first one contains "[Schema"
                    for (int i = 1, splitLength = parts.length; i < splitLength; i++) {
                        String part = parts[i];
                        String[] subParts = part.split(":");

                        switch (subParts[0]) {
                            case "VL":
                                m.makeVertexLabel(subParts[1]).make();
                                break;
                            case "EL":
                                m.makeEdgeLabel(subParts[1]).make();
                                break;
                            case "PK":
                                m.makePropertyKey(subParts[1]).dataType(Class.forName(subParts[2])).make();
                                break;
                        }
                    }

                    m.commit();
                } else if (query.startsWith("[CI")) {
                    String[] parts = query.split(":");

                    String indexName = parts[1];
                    boolean composite = Boolean.parseBoolean(parts[2]);
                    String label = parts[3];
                    String[] properties = parts[4].substring(1, parts[4].length() - 2).split(", ");
                    Set<String> propertySet = new HashSet<>(Arrays.asList(properties));

                    new JanusCreateIndexQuery(label, propertySet, indexName, composite).execute(state);
                } else if (query.startsWith("[DI")) {
                    String[] parts = query.split(":");
                    String indexName = parts[1].substring(0, parts[1].length() - 1);

                    new JanusRemoveIndexQuery(indexName).execute(state);
                } else {
                    new JanusQuery(query).execute(state);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
