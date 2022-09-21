package ch.ethz.ast.gdbmeter.redis;

import ch.ethz.ast.gdbmeter.common.ExpectedErrors;

public class RedisUtil {

    public static void addFunctionErrors(ExpectedErrors errors) {
        errors.add("length must be positive integer");
        errors.add("start must be positive integer");
        errors.add("Type mismatch: expected Integer but was Null");
    }

    public static void addArithmeticErrors(ExpectedErrors errors) {
        errors.add("Division by zero");
    }

}
