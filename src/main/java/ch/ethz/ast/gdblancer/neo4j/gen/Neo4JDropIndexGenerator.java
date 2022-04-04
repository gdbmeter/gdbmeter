package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;

public class Neo4JDropIndexGenerator {

    private final Neo4JDBSchema schema;
    private final StringBuilder query = new StringBuilder();

    public Neo4JDropIndexGenerator(Neo4JDBSchema schema) {
        this.schema = schema;
    }

    public static String dropIndex(Neo4JDBSchema schema) {
        return new Neo4JDropIndexGenerator(schema).generateDropIndex();
    }

    private String generateDropIndex() {
        if (!schema.hasIndices()) {
            throw new IgnoreMeException();
        }

        String name = schema.getRandomIndex();
        schema.removeIndex(name);

        query.append(String.format("DROP INDEX %s", name));
        return query.toString();
    }

}
