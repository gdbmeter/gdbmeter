package ch.ethz.ast.gdbmeter.janus.schema;

// See: https://docs.janusgraph.org/schema/#property-key-data-type
public enum JanusType {

    STRING(String.class),
    CHARACTER(Character.class),
    BOOLEAN(Boolean.class),
    BYTE(Byte.class),
    SHORT(Short.class),
    INTEGER(Integer.class),
    LONG(Long.class),
    FLOAT(Float.class),
    DOUBLE(Double.class),
    DATE(java.util.Date.class),
    UUID(java.util.UUID.class);
    // TODO: Support geoshape

    private final Class<?> javaClass;

    JanusType(Class<?> javaClass) {
        this.javaClass = javaClass;
    }

    public Class<?> getJavaClass() {
        return javaClass;
    }
}
