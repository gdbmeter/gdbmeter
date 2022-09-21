package ch.ethz.ast.gdbmeter.redis.schema;

import ch.ethz.ast.gdbmeter.util.Randomization;

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
