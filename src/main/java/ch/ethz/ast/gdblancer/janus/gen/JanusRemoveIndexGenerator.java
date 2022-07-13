package ch.ethz.ast.gdblancer.janus.gen;

import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.janus.query.JanusQueryAdapter;
import ch.ethz.ast.gdblancer.janus.query.JanusRemoveIndexQuery;
import ch.ethz.ast.gdblancer.janus.schema.JanusType;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;

public class JanusRemoveIndexGenerator {

    public static JanusQueryAdapter dropIndex(Schema<JanusType> schema) {
        if (!schema.hasIndices()) {
            throw new IgnoreMeException();
        }

        return new JanusRemoveIndexQuery(schema.getRandomIndex());
    }

}
