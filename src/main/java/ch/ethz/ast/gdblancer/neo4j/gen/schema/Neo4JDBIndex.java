package ch.ethz.ast.gdblancer.neo4j.gen.schema;

import java.util.Set;

public class Neo4JDBIndex {

    private final String label;
    private final Set<String> propertyNames;

    public Neo4JDBIndex(String label, Set<String> propertyNames) {
        this.label = label;
        this.propertyNames = propertyNames;
    }


    public String getLabel() {
        return label;
    }

    public Set<String> getPropertyNames() {
        return propertyNames;
    }

}
