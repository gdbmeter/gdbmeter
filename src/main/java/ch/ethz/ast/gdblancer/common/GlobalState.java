package ch.ethz.ast.gdblancer.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalState<C extends Connection> {

    private C connection;
    private final Logger LOGGER = LoggerFactory.getLogger(GlobalState.class);

    public Logger getLogger() {
        return LOGGER;
    }

    public C getConnection() {
        return connection;
    }

    public void setConnection(C connection) {
        this.connection = connection;
    }

    public boolean execute(Query query) {
        LOGGER.info(query.getQuery());

        try {
            connection.execute(query);
        } catch (Exception exception) {
            if (query.getExpectedErrors().isExpected(exception)) {
                return false;
            } else {

                // See: #12869
                if (exception instanceof IndexOutOfBoundsException) {
                    return false;
                }

                throw new AssertionError(query.getQuery(), exception);
            }
        }

        return true;
    }
}
