package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JCreateIndexGenerator {

    private final StringBuilder query = new StringBuilder();
    private static final String INDEX_PREFIX = "i";
    private static int indexCounter = 0;

    public static String createIndex() {
        return new Neo4JCreateIndexGenerator().generateCreateIndex();
    }

    private String generateCreateIndex() {
        query.append("CREATE ");

        if (Randomization.getBoolean()) {
            query.append("BTREE ");
        }

        // TODO: Somehow generate randomized unique index names
        query.append("INDEX ");
        query.append(INDEX_PREFIX);
        query.append(indexCounter++);
        query.append(" ");

        if (Randomization.getBoolean()) {
            query.append("IF NOT EXISTS ");
        }

        query.append("FOR (n:");
        // TODO: Select a random label instead
        query.append(Neo4JGraphGenerator.generateValidName());
        query.append(") ");
        query.append("ON (n.");
        // TODO: Select a random property name of above label
        query.append(Neo4JGraphGenerator.generateValidName());
        query.append(")");

        return query.toString();
    }

}
