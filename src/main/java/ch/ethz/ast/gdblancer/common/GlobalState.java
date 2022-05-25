package ch.ethz.ast.gdblancer.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;

public class GlobalState<C extends Connection> {

    private C connection;
    private final Logger LOGGER = LoggerFactory.getLogger(GlobalState.class);
    private int executedQueries;
    private LocalTime startTime;

    public Logger getLogger() {
        return LOGGER;
    }

    public C getConnection() {
        return connection;
    }

    public void setConnection(C connection) {
        this.connection = connection;
    }

    public void increaseExecutedQueries() {
        executedQueries++;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public int getExecutedQueries() {
        return executedQueries;
    }

    public void setStartTime() {
        startTime = LocalTime.now();
        executedQueries = 0;
    }

}
