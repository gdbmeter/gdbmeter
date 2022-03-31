package ch.ethz.ast.gdblancer.neo4j;

import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;

import java.nio.file.FileSystems;

public class Neo4JConnection {

    private final GraphDatabaseService databaseService;

    public Neo4JConnection() {
        var managementService = new DatabaseManagementServiceBuilder(FileSystems.getDefault().getPath("database", "gdblancer")).build();
        Runtime.getRuntime().addShutdownHook(new Thread(managementService::shutdown));

        this.databaseService = managementService.database("neo4j");
        this.cleanup();
    }

    public void executeQuery(String query) {
        System.out.println(query); // TODO: use log
        this.databaseService.executeTransactionally(query);
    }

    public void cleanup() {
        this.databaseService.executeTransactionally("MATCH (n) DETACH DELETE n");
    }

}
