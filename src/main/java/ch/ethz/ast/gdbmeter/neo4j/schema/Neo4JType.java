package ch.ethz.ast.gdbmeter.neo4j.schema;

import ch.ethz.ast.gdbmeter.util.Randomization;

public enum Neo4JType {

    INTEGER,
    FLOAT,
    STRING,
    BOOLEAN,
    DURATION,
    DATE,
    LOCAL_TIME,
    POINT;

    public static Neo4JType getRandom() {
        return Randomization.fromOptions(Neo4JType.values());
    }

}
