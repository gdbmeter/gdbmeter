package ch.ethz.ast.gdblancer.janus;

import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.QueryReplay;
import ch.ethz.ast.gdblancer.janus.query.JanusQuery;
import org.janusgraph.core.schema.JanusGraphManagement;

import java.util.List;

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
                } else {
                    new JanusQuery(query).execute(state);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
