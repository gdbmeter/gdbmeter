package ch.ethz.ast.gdbmeter.neo4j;

import ch.ethz.ast.gdbmeter.common.ExpectedErrors;

public class Neo4JUtil {

    public static void addRegexErrors(ExpectedErrors errors) {
        errors.add("Invalid Regex: Unclosed character class");
        errors.add("Invalid Regex: Illegal repetition");
        errors.add("Invalid Regex: Unclosed group");
        errors.add("Invalid Regex: Dangling meta character");
        errors.add("Invalid Regex: Illegal/unsupported escape sequence");
        errors.add("Invalid Regex: Unmatched closing");
        errors.add("Invalid Regex: Unclosed counted closure");
        errors.add("Invalid Regex: Illegal character range");
        errors.add("Invalid Regex: Unclosed character family");
        errors.add("Invalid Regex: Unknown inline modifier");
        errors.add("Invalid Regex: \\k is not followed by '<' for named capturing group");
        errors.add("Invalid Regex: Unclosed hexadecimal escape sequence");      // RETURN (""=~"\x{a")
        errors.add("Invalid Regex: capturing group name does not start with a Latin letter");
        errors.add("Invalid Regex: Unexpected internal error");
        errors.add("Invalid Regex: Unknown character property name");           // RETURN (""=~"5\\P")
        errors.add("Invalid Regex: Illegal octal escape sequence");             // RETURN (""=~"\\0q")
        errors.add("Invalid Regex: Illegal hexadecimal escape sequence");       // RETURN (""=~"\\xp")
        errors.add("Invalid Regex: Illegal character name escape sequence");    // RETURN (""=~"\NZ")
        errors.add("Invalid Regex: Illegal Unicode escape sequence");           // RETURN ""=~("\\uA")
        errors.add("Invalid Regex: Illegal control escape sequence");           // RETURN ""=~("\\c")
        errors.addRegex("Invalid Regex: Unescaped trailing backslash near index [0-9]+\n.*");
    }

    public static void addArithmeticErrors(ExpectedErrors errors) {
        errors.add("/ by zero");
        errors.add("cannot be represented as an integer");
        errors.add("long overflow");
        errors.addRegex("integer, (-??)(\\+??)[0-9]+([.][0-9E]*)?, is too large");
    }

    public static void addFunctionErrors(ExpectedErrors errors) {
        errors.add("Invalid input for length value in function 'left()': Expected a numeric value but got: NO_VALUE");
        errors.add("Invalid input for length value in function 'right()': Expected a numeric value but got: NO_VALUE");
        errors.add("Invalid input for length value in function 'substring()': Expected a numeric value but got: NO_VALUE");
        errors.add("Invalid input for start value in function 'substring()': Expected a numeric value but got: NO_VALUE");
        errors.addRegex("Invalid input for length value in function 'substring\\(\\)': Expected an integer between -2147483648 and 2147483647, but got: .*");
        errors.addRegex("Invalid input for start value in function 'substring\\(\\)': Expected an integer between -2147483648 and 2147483647, but got: .*");
        errors.addRegex("negative length"); // for right functions
    }

}
