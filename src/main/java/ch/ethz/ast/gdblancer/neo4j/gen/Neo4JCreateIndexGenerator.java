package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBIndex;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Set;

public class Neo4JCreateIndexGenerator {

    enum INDEX_TYPES {
        NODE_INDEX,
        RELATIONSHIP_INDEX,
        TEXT_INDEX
    }

    private final Neo4JDBSchema schema;
    private final ExpectedErrors errors = new ExpectedErrors();
    private final StringBuilder query = new StringBuilder();

    public Neo4JCreateIndexGenerator(Neo4JDBSchema schema) {
        this.schema = schema;
    }

    public static Query createIndex(Neo4JDBSchema schema) {
        return new Neo4JCreateIndexGenerator(schema).generateCreateIndex();
    }

    private Query generateCreateIndex() {
        switch (Randomization.fromOptions(INDEX_TYPES.values())) {
            // TODO: Merge first two cases
            case NODE_INDEX:
                generateNodeIndex();
                break;
            case RELATIONSHIP_INDEX:
                generateRelationshipIndex();
                break;
            case TEXT_INDEX:
                generateNodeTextIndex();
                break;
        }

        return new Query(query.toString(), errors);
    }

    private void generateNodeTextIndex() {
        query.append("CREATE TEXT INDEX ");

        Neo4JDBIndex index = schema.generateRandomTextIndex();
        String name = schema.generateRandomIndexName();

        query.append(name);
        query.append(" ");

        // TODO: Maybe choose same name deliberately in this case?
        if (Randomization.getBoolean()) {
            query.append("IF NOT EXISTS ");
        } else {
            errors.add("There already exists an index");
        }

        query.append(String.format("FOR (n:%s) ON (n.%s)", index.getLabel(), index.getPropertyNames().toArray()[0]));
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

        query.append(name);
        query.append(" ");

        // TODO: Maybe choose same name deliberately in this case?
        if (Randomization.getBoolean()) {
            query.append("IF NOT EXISTS ");
        } else {
            errors.add("There already exists an index");
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

        query.append(name);
        query.append(" ");

        // TODO: Maybe choose same name deliberately in this case?
        if (Randomization.getBoolean()) {
            query.append("IF NOT EXISTS ");
        } else {
            errors.add("There already exists an index");
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
