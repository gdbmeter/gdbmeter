package ch.ethz.ast.gdbmeter.janus.gen;

import ch.ethz.ast.gdbmeter.common.Query;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.janus.JanusConnection;
import ch.ethz.ast.gdbmeter.janus.query.JanusRemoveIndexQuery;
import ch.ethz.ast.gdbmeter.janus.schema.JanusType;
import ch.ethz.ast.gdbmeter.util.IgnoreMeException;

public class JanusRemoveIndexGenerator {

    public static Query<JanusConnection> dropIndex(Schema<JanusType> schema) {
        if (!schema.hasIndices()) {
            throw new IgnoreMeException();
        }

        return new JanusRemoveIndexQuery(schema.getRandomIndex());
    }

}
