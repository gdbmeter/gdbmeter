package ch.ethz.ast.gdblancer.cypher.gen;

import ch.ethz.ast.gdblancer.cypher.schema.CypherIndex;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Set;

public abstract class CypherCreateIndexGenerator {

    enum INDEX_TYPES {
        NODE_INDEX,
        RELATIONSHIP_INDEX,
        TEXT_INDEX
    }

    private final CypherSchema schema;
    protected final StringBuilder query = new StringBuilder();

    public CypherCreateIndexGenerator(CypherSchema schema) {
        this.schema = schema;
    }

    protected void generateCreateIndex() {
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
    }

    private void generateNodeTextIndex() {
        query.append("CREATE TEXT INDEX ");

        CypherIndex index = schema.generateRandomTextIndex();

        query.append(schema.generateRandomIndexName());
        query.append(" ");

        // TODO: Maybe choose same name deliberately in this case?
        if (Randomization.getBoolean()) {
            query.append("IF NOT EXISTS ");
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
        CypherIndex index = schema.generateRandomNodeIndex();

        query.append(schema.generateRandomIndexName());
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
        CypherIndex index = schema.generateRandomRelationshipIndex();

        query.append(schema.generateRandomIndexName());
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
