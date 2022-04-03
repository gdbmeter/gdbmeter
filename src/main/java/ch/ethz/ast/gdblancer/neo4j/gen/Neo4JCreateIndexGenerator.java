package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.neo4j.gen.schema.MongoDBSchema;
import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JCreateIndexGenerator {

    private final MongoDBSchema schema;
    private final StringBuilder query = new StringBuilder();

    public Neo4JCreateIndexGenerator(MongoDBSchema schema) {
        this.schema = schema;
    }

    public static String createIndex(MongoDBSchema schema) {
        return new Neo4JCreateIndexGenerator(schema).generateCreateIndex();
    }

    private String generateCreateIndex() {
        query.append("CREATE ");

        if (Randomization.getBoolean()) {
            query.append("BTREE ");
        }

        query.append("INDEX ");
        query.append(schema.generateIndexName());
        query.append(" ");

        // TODO: Maybe choose same name deliberately in this case?
        if (Randomization.getBoolean()) {
            query.append("IF NOT EXISTS ");
        }

        String label = schema.getRandomLabel();
        String property = schema.getRandomPropertyForLabel(label);
        query.append(String.format("FOR (n:%s) ON (n.%s)", label, property));

        return query.toString();
    }

}
