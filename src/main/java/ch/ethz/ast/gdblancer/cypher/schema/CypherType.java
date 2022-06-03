package ch.ethz.ast.gdblancer.cypher.schema;

import ch.ethz.ast.gdblancer.util.Randomization;

public enum CypherType {

    INTEGER,
    FLOAT,
    STRING,
    BOOLEAN,
    DURATION,
    DATE,
    LOCAL_TIME,
    POINT;

    public static CypherType getRandom() {
        return Randomization.fromOptions(CypherType.values());
    }

}
