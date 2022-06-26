package ch.ethz.ast.gdblancer.common.schema;

import java.util.Set;

public class CypherIndex {

    private final String label;
    private final Set<String> propertyNames;

    public CypherIndex(String label, Set<String> propertyNames) {
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
