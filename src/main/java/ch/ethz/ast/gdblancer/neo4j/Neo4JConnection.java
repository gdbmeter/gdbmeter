package ch.ethz.ast.gdblancer.neo4j;

import ch.ethz.ast.gdblancer.common.Connection;
import ch.ethz.ast.gdblancer.common.Query;
import org.apache.commons.io.FileUtils;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.schema.IndexDefinition;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Neo4JConnection implements Connection {

    private final Path databasePath = FileSystems.getDefault().getPath("database");
    private DatabaseManagementService managementService;
    private GraphDatabaseService databaseService;

    @Override
    public void connect() throws IOException {
        // Clear the database by deleting the folder
        // This seems to be the only option in the Neo4J community edition
        FileUtils.deleteDirectory(databasePath.toFile());

        this.managementService = new DatabaseManagementServiceBuilder(databasePath).build();
        this.databaseService = managementService.database("neo4j");
    }

    public List<Map<String, Object>> execute(Query<Neo4JConnection> query) {
        List<Map<String, Object>> resultRows;

        try (Transaction transaction = this.databaseService.beginTx()) {
            Result result = transaction.execute(query.getQuery());
            resultRows = result.stream().collect(Collectors.toList());
            transaction.commit();
        }

        return resultRows;
    }

    @Override
    public void close()  {
        this.managementService.shutdown();
    }

    public Set<String> getIndexNames() {
        Set<String> indices = new HashSet<>();

        try (Transaction transaction = this.databaseService.beginTx()) {
            for (IndexDefinition index : transaction.schema().getIndexes()) {
                indices.add(index.getName());
            }
        }

        return indices;
    }

}
