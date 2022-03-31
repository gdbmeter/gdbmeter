package ch.ethz.ast.gdblancer.neo4j;

public enum PropertyType {

    INTEGER,
    FLOAT,
    STRING,
    BOOLEAN,
    NULL // This is not a type perse but we want to generate null values
    // TODO: Maybe add support for Point, Date, Time, Duration, List

}
