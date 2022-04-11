package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBIndex;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Set;

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
        // TODO: Merge both index methods
        if (Randomization.getBoolean()) {
            generateNodeIndex();
        } else {
            generateRelationshipIndex();
        }

        return query.toString();
    }

    private void generateNodeIndex() {
        query.append("CREATE ");

        if (Randomization.getBoolean()) {
            query.append("BTREE ");
        }

        query.append("INDEX ");

        // TODO: Maybe add support for unnamed indices
        Neo4JDBIndex index = schema.generateRandomNodeIndex();
        String name = schema.generateRandomIndexName();
        schema.registerIndex(name, index);

        query.append(name);
        query.append(" ");

        // TODO: Maybe choose same name deliberately in this case?
        if (Randomization.getBoolean()) {
            query.append("IF NOT EXISTS ");
        }

        query.append(String.format("FOR (n:%s) ", index.getLabel()));
        generateOnClause(index.getPropertyNames());
    }

    private void generateRelationshipIndex() {
        query.append("CREATE ");

        if (Randomization.getBoolean()) {
            query.append("BTREE ");
        }

        query.append("INDEX ");

        // TODO: Maybe add support for unnamed indices
        Neo4JDBIndex index = schema.generateRandomRelationshipIndex();
        String name = schema.generateRandomIndexName();
        schema.registerIndex(name, index);

        query.append(name);
        query.append(" ");

        // TODO: Maybe choose same name deliberately in this case?
        if (Randomization.getBoolean()) {
            query.append("IF NOT EXISTS ");
        }

        query.append(String.format("FOR ()-[n:%s]-() ", index.getLabel()));
        generateOnClause(index.getPropertyNames());
    }

    private void generateOnClause(Set<String> properties) {
        query.append("ON (");
        String delimiter = "";

        for (String property : properties) {
            query.append(delimiter);
            query.append(String.format("n.%s", property));
            delimiter = ", ";
        }

        query.append(")");
    }

}
