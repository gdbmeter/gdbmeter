package ch.ethz.ast.gdblancer.janus;

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
    // DATE, // java.util.Date
    // GEOSHAPE, // Point, Circle or Box
    UUID(java.util.UUID.class);

    private final Class<?> javaClass;

    JanusType(Class<?> javaClass) {
        this.javaClass = javaClass;
    }

    public Class<?> getJavaClass() {
        return javaClass;
    }
}
