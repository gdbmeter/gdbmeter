package ch.ethz.ast.gdblancer.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Allows replaying queries from a file.
 */
public abstract class QueryReplay {

    /**
     * Executes all queries from file.
     * @param file  The file to read from.
     */
    public void replayFromFile(File file) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }

        executeQueries(lines);
    }

    protected abstract void executeQueries(List<String> queries);

}
