package ch.ethz.ast.gdblancer.neo4j;

import ch.ethz.ast.gdblancer.common.Connection;
import ch.ethz.ast.gdblancer.common.Query;
import org.apache.commons.io.FileUtils;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Neo4JConnection implements Connection {

    private final Path databasePath = FileSystems.getDefault().getPath("database");
    private DatabaseManagementService managementService;
    private GraphDatabaseService databaseService;

    @Override
    public void close()  {
        this.managementService.shutdown();
    }

    // TODO: Maybe move this part to the constructor?
    @Override
    public void connect() throws IOException {
        this.managementService = new DatabaseManagementServiceBuilder(databasePath).build();
        this.databaseService = managementService.database("neo4j");

        this.clearDatabase();
    }

    @Override
    public void execute(Query query) {
        this.databaseService.executeTransactionally(query.getQuery());
    }

    @Override
    public void clearDatabase() throws IOException {
        FileUtils.deleteDirectory(databasePath.toFile());
    }

}
