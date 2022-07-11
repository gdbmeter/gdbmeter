package ch.ethz.ast.gdblancer.janus;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.Query;

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
