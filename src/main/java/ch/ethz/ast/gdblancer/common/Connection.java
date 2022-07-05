package ch.ethz.ast.gdblancer.common;

public interface Connection extends AutoCloseable {

    /**
     * Connect to the database and clear it.
     */
    void connect() throws Exception;

}
