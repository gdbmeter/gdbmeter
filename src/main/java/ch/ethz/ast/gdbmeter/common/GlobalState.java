package ch.ethz.ast.gdbmeter.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class GlobalState<C extends Connection> {

    private C connection;
    private final Logger LOGGER = LoggerFactory.getLogger(GlobalState.class);

    public C getConnection() {
        return connection;
    }

    public void setConnection(C connection) {
        this.connection = connection;
    }

    private final List<String> currentExecutionLog = new LinkedList<>();

    public void logCurrentExecution() {
        for (String query : currentExecutionLog) {
            LOGGER.info(query);
        }
    }

    public void appendToLog(String query) {
        currentExecutionLog.add(query);
    }

    public void clearLog() {
        currentExecutionLog.clear();
    }

}
