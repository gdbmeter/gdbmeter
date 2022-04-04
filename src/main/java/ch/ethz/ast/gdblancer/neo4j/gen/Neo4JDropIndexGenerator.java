package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.neo4j.gen.schema.MongoDBSchema;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;

public class Neo4JDropIndexGenerator {

    private final MongoDBSchema schema;
    private final StringBuilder query = new StringBuilder();

    public Neo4JDropIndexGenerator(MongoDBSchema schema) {
        this.schema = schema;
    }

    public static String dropIndex(MongoDBSchema schema) {
        return new Neo4JDropIndexGenerator(schema).generateDropIndex();
    }

    private String generateDropIndex() {
        if (!schema.hasIndices()) {
            throw new IgnoreMeException();
        }

        query.append(String.format("DROP INDEX %s", schema.getRandomIndex()));
        return query.toString();
    }

}
