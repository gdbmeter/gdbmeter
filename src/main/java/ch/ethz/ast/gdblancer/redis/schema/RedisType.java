package ch.ethz.ast.gdblancer.redis.schema;

import ch.ethz.ast.gdblancer.util.Randomization;

public enum RedisType {

    INTEGER,
    FLOAT,
    STRING,
    BOOLEAN,
    POINT;

    public static RedisType getRandom() {
        return Randomization.fromOptions(RedisType.values());
    }

}
