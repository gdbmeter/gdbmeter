package ch.ethz.ast.gdblancer.neo4j;

import org.apache.commons.io.FileUtils;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Neo4JConnection implements AutoCloseable {

    private final Logger LOGGER = LoggerFactory.getLogger(Neo4JConnection.class);
    private final DatabaseManagementService managementService;
    private final GraphDatabaseService databaseService;

    public Neo4JConnection() {
        Path databasePath = FileSystems.getDefault().getPath("database");
        this.managementService = new DatabaseManagementServiceBuilder(databasePath).build();

        // The community edition doesn't support the drop database option
        try {
            FileUtils.deleteDirectory(databasePath.toFile());
        } catch (IOException e) {
            LOGGER.error("Failed to clear database folder", e);
        }

        this.databaseService = managementService.database("neo4j");
    }

    public void executeQuery(String query) {
        LOGGER.info(query);
        this.databaseService.executeTransactionally(query);
    }

    @Override
    public void close()  {
        LOGGER.info("Closing database");
        this.managementService.shutdown();
    }
}
