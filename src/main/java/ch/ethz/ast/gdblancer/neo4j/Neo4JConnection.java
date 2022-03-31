package ch.ethz.ast.gdblancer.neo4j;

import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.FileSystems;

public class Neo4JConnection {

    private final Logger LOGGER = LoggerFactory.getLogger(Neo4JConnection.class);
    private final GraphDatabaseService databaseService;

    public Neo4JConnection() {
        var managementService = new DatabaseManagementServiceBuilder(FileSystems.getDefault().getPath("database", "gdblancer")).build();
        Runtime.getRuntime().addShutdownHook(new Thread(managementService::shutdown));

        this.databaseService = managementService.database("neo4j");
        this.cleanup();
    }

    public void executeQuery(String query) {
        LOGGER.info(query);
        this.databaseService.executeTransactionally(query);
    }

    public void cleanup() {
        executeQuery("MATCH (n) DETACH DELETE n");
    }

}
