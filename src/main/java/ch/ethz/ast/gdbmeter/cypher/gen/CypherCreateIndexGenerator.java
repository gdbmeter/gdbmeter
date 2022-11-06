package ch.ethz.ast.gdbmeter.cypher.gen;

import ch.ethz.ast.gdbmeter.common.schema.Index;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.util.Randomization;

import java.util.Set;

public abstract class CypherCreateIndexGenerator<T> {

    protected final Schema<T> schema;
    protected final StringBuilder query = new StringBuilder();
    private final T stringType;

    public CypherCreateIndexGenerator(Schema<T> schema, T stringType) {
        this.schema = schema;
        this.stringType = stringType;
    }

    protected void generateCreateIndex() {
        switch (Randomization.fromOptions(CypherIndexTypes.values())) {
            case NODE_INDEX -> generateNodeIndex(schema.generateRandomNodeIndex());
            case RELATIONSHIP_INDEX -> generateRelationshipIndex(schema.generateRandomRelationshipIndex());
            case TEXT_INDEX -> generateTextIndex(schema.generateRandomTextIndex(stringType));
        }
    }

    protected abstract void generateNodeIndex(Index index);
    protected abstract void generateRelationshipIndex(Index index);
    protected abstract void generateTextIndex(Index index);

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
