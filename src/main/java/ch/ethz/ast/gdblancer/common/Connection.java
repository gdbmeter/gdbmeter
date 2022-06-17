package ch.ethz.ast.gdblancer.common;

import java.io.IOException;

public interface Connection extends AutoCloseable {

    /**
     * Connect to the database and clear it.
     */
    void connect() throws IOException;

}
