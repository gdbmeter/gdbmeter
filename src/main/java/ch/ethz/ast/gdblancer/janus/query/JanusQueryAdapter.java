package ch.ethz.ast.gdblancer.janus.query;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.janus.JanusConnection;

public abstract class JanusQueryAdapter extends Query<JanusConnection> {

    public JanusQueryAdapter() {
    }

    public JanusQueryAdapter(boolean couldAffectSchema) {
        super(couldAffectSchema);
    }

    public JanusQueryAdapter(ExpectedErrors expectedErrors) {
        super(expectedErrors);
    }

    public JanusQueryAdapter(ExpectedErrors expectedErrors, boolean couldAffectSchema) {
        super(expectedErrors, couldAffectSchema);
    }

}
