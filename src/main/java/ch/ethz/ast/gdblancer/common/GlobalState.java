package ch.ethz.ast.gdblancer.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalState<C extends Connection> {

    private C connection;
    private final Logger LOGGER = LoggerFactory.getLogger(GlobalState.class);
    private int executedQueries;

    public Logger getLogger() {
        return LOGGER;
    }

    public C getConnection() {
        return connection;
    }

    public void setConnection(C connection) {
        this.connection = connection;
    }

}
