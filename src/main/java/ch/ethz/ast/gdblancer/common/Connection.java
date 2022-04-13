package ch.ethz.ast.gdblancer.common;

import java.io.IOException;

public interface Connection extends AutoCloseable {

    void connect() throws IOException;

    void execute(Query query) throws Exception;

}
