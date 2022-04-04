package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBIndex;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JCreateIndexGenerator {

    private final Neo4JDBSchema schema;
    private final StringBuilder query = new StringBuilder();

    public Neo4JCreateIndexGenerator(Neo4JDBSchema schema) {
        this.schema = schema;
    }

    public static String createIndex(Neo4JDBSchema schema) {
        return new Neo4JCreateIndexGenerator(schema).generateCreateIndex();
    }

    private String generateCreateIndex() {
        query.append("CREATE ");

        if (Randomization.getBoolean()) {
            query.append("BTREE ");
        }

        query.append("INDEX ");

        // TODO: Maybe add support for unnamed indices
        Neo4JDBIndex index = schema.generateRandomIndex();
        String name = schema.generateRandomIndexName();
        schema.registerIndex(name, index);

        query.append(name);
        query.append(" ");

        // TODO: Maybe choose same name deliberately in this case?
        if (Randomization.getBoolean()) {
            query.append("IF NOT EXISTS ");
        }

        query.append(String.format("FOR (n:%s) ON (n.%s)",
                index.getLabel(),
                index.getPropertyNames().toArray(new String[0])[0]));

        return query.toString();
    }

}
