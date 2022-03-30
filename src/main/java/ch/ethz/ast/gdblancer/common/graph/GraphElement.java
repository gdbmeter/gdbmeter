package ch.ethz.ast.gdblancer.common.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GraphElement {

    private long id;
    private final Map<String, Object> properties;

    GraphElement(Map<String, Object> properties) {
        this.properties = new HashMap<>(properties);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Object getPropertyValue(String name) {
        return properties.get(name);
    }

    public Set<String> getProperties() {
        return properties.keySet();
    }

}
