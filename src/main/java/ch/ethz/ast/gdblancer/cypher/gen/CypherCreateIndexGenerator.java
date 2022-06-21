package ch.ethz.ast.gdblancer.cypher.gen;

import ch.ethz.ast.gdblancer.cypher.schema.CypherIndex;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Set;

public abstract class CypherCreateIndexGenerator {

    protected final CypherSchema schema;
    protected final StringBuilder query = new StringBuilder();

    public CypherCreateIndexGenerator(CypherSchema schema) {
        this.schema = schema;
    }

    protected void generateCreateIndex() {
        switch (Randomization.fromOptions(CypherIndexTypes.values())) {
            case NODE_INDEX:
                generateNodeIndex(schema.generateRandomNodeIndex());
                break;
            case RELATIONSHIP_INDEX:
                generateRelationshipIndex(schema.generateRandomRelationshipIndex());
                break;
            case TEXT_INDEX:
                generateTextIndex(schema.generateRandomTextIndex());
                break;
        }
    }

    protected abstract void generateNodeIndex(CypherIndex index);
    protected abstract void generateRelationshipIndex(CypherIndex index);
    protected abstract void generateTextIndex(CypherIndex index);

    protected void generateOnClause(Set<String> properties) {
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
