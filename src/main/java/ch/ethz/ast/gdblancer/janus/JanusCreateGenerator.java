package ch.ethz.ast.gdblancer.janus;

import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.schema.Schema;

import java.util.List;
import java.util.Map;

public class JanusCreateGenerator {
    
    public static JanusQuery createEntities(Schema<JanusType> schema) {

        return new JanusQuery() {
            @Override
            public String getQuery() {
                return null;
            }

            @Override
            public boolean execute(GlobalState<JanusConnection> globalState) {
                return false;
            }

            @Override
            public List<Map<String, Object>> executeAndGet(GlobalState<JanusConnection> globalState) {
                return null;
            }
        };
    }

}
