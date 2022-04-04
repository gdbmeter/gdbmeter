package ch.ethz.ast.gdblancer.neo4j.gen.schema;

import java.util.Objects;
import java.util.Set;

public class MongoDBIndex {

    private final String label;
    private final Set<String> propertyNames;

    public MongoDBIndex(String label, Set<String> propertyNames) {
        this.label = label;
        this.propertyNames = propertyNames;
    }


    public String getLabel() {
        return label;
    }

    public Set<String> getPropertyNames() {
        return propertyNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MongoDBIndex that = (MongoDBIndex) o;
        return Objects.equals(label, that.label) && Objects.equals(propertyNames, that.propertyNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, propertyNames);
    }

}
