package ch.ethz.ast.gdblancer.neo4j.gen.ast;

import ch.ethz.ast.gdblancer.neo4j.gen.ast.Neo4JBinaryArithmeticOperation.ArithmeticOperator;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JType;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Neo4JExpressionGenerator {

    private static final int MAX_DEPTH = 3;

    private enum IntegerConstantFormat {
        NORMAL_INTEGER,
        HEX_INTEGER,
        OCTAL_INTEGER
    }

    public static Neo4JExpression generateConstant(Neo4JType type) {
        if (Randomization.smallBiasProbability()) {
            return new Neo4JConstant.NullConstant();
        }

        switch (type) {
            case INTEGER:
                switch (Randomization.fromOptions(IntegerConstantFormat.values())) {
                    case NORMAL_INTEGER:
                        return new Neo4JConstant.IntegerConstant(Randomization.getInteger());
                    case HEX_INTEGER:
                        return new Neo4JConstant.IntegerHexConstant(Randomization.getInteger());
                    case OCTAL_INTEGER:
                        return new Neo4JConstant.IntegerOctalConstant(Randomization.getInteger(), Neo4JConstant.IntegerOctalConstant.OctalPrefix.getRandom());
                }
            case BOOLEAN:
                return new Neo4JConstant.BooleanConstant(Randomization.getBoolean());
            case FLOAT:
                return new Neo4JConstant.FloatConstant(Randomization.nextFloat());
            case STRING:
                return new Neo4JConstant.StringConstant(Randomization.getString());
            case POINT:
                if (Randomization.getBoolean()) {
                    return new Neo4JConstant.PointConstant(Randomization.getDouble(), Randomization.getDouble());
                } else {
                    return new Neo4JConstant.PointConstant(Randomization.getDouble(), Randomization.getDouble(), Randomization.getDouble());
                }
            case DURATION:
                Map<String, Long> datePart;
                Map<String, Long> timePart;

                do {
                    datePart = new LinkedHashMap<>();

                    for (String current : new String[]{"Y", "M", "W", "D"}) {
                        if (Randomization.getBoolean()) {
                            datePart.put(current, Randomization.getPositiveInt());
                        }
                    }

                    timePart = new LinkedHashMap<>();

                    for (String current : new String[]{"H", "M", "S"}) {
                        if (Randomization.getBoolean()) {
                            timePart.put(current, Randomization.getPositiveInt());
                        }
                    }
                } while (datePart.isEmpty() && timePart.isEmpty());

                return new Neo4JConstant.DurationConstant(datePart, timePart);
            case LOCAL_TIME:
                int hours = Randomization.nextInt(0, 24);
                String separator;
                Integer minutes = null;
                Integer seconds = null;
                String nanoSecondSeparator = null;
                Integer nanoSeconds = null;

                if (Randomization.getBoolean()) {
                    separator = ":";
                } else {
                    separator = "";
                }

                if (Randomization.getBoolean()) {
                    minutes = Randomization.nextInt(0, 59);

                    if (Randomization.getBoolean()) {
                        seconds = Randomization.nextInt(0, 59);

                        if (Randomization.getBoolean()) {
                            if (Randomization.getBoolean()) {
                                nanoSecondSeparator = ".";
                            } else {
                                nanoSecondSeparator = ",";
                            }

                            nanoSeconds = Randomization.nextInt(0, 1000000000);
                        }
                    }
                }

                return new Neo4JConstant.LocalTimeConstant(hours,
                        separator,
                        minutes,
                        seconds,
                        nanoSecondSeparator,
                        nanoSeconds);
            case DATE:
                return new Neo4JConstant.DateConstant(Randomization.getBoolean(),
                        Randomization.nextInt(0, 1000),
                        Randomization.nextInt(1, 13),
                        Randomization.nextInt(1, 32));
            default:
                throw new AssertionError(type);
        }
    }

    private enum BooleanExpression {
        BINARY_LOGICAL_OPERATOR, NOT,
        POSTFIX_OPERATOR, BINARY_COMPARISON,
        BINARY_STRING_OPERATOR, REGEX, FUNCTION
    }

    // TODO: Support IN_OPERATION
    private Neo4JExpression generateBooleanExpression(int depth) {
        BooleanExpression option = Randomization.fromOptions(BooleanExpression.values());

        switch (option) {
            case BINARY_LOGICAL_OPERATOR:
                Neo4JExpression first = generateExpression(depth + 1, Neo4JType.BOOLEAN);
                int nr = Randomization.smallNumber() + 1;

                for (int i = 0; i < nr; i++) {
                    first = new Neo4JBinaryLogicalOperation(first,
                            generateExpression(depth + 1, Neo4JType.BOOLEAN),
                            Neo4JBinaryLogicalOperation.BinaryLogicalOperator.getRandom());
                }

                return first;
            case NOT:
                return new Neo4JPrefixOperation(generateExpression(depth + 1, Neo4JType.BOOLEAN),
                        Neo4JPrefixOperation.PrefixOperator.NOT);
            case POSTFIX_OPERATOR:
                return new Neo4JPostfixOperation(generateExpression(depth + 1, Randomization.fromOptions(Neo4JType.values())),
                        Neo4JPostfixOperation.PostfixOperator.getRandom());
            case BINARY_COMPARISON:
                return generateComparison(depth, Randomization.fromOptions(Neo4JType.values()));
            case BINARY_STRING_OPERATOR:
                return new Neo4JBinaryStringOperation(generateExpression(depth + 1, Neo4JType.STRING),
                        generateExpression(depth + 1, Neo4JType.STRING),
                        Neo4JBinaryStringOperation.BinaryStringOperation.getRandom());
            case REGEX:
                return new Neo4JRegularExpression(generateExpression(depth + 1, Neo4JType.STRING),
                        generateExpression(depth + 1, Neo4JType.STRING));
            case FUNCTION:
                return generateFunction(depth + 1, Neo4JType.BOOLEAN);
            default:
                throw new AssertionError(option);
        }
    }

    private Neo4JExpression generateComparison(int depth, Neo4JType type) {
        Neo4JExpression left = generateExpression(depth + 1, type);
        Neo4JExpression right = generateExpression(depth + 1, type);
        return new Neo4JBinaryComparisonOperation(left, right,
                Neo4JBinaryComparisonOperation.BinaryComparisonOperator.getRandom());
    }

    private enum IntExpression {
        UNARY_OPERATION, BINARY_ARITHMETIC_EXPRESSION, FUNCTION
    }

    private Neo4JExpression generateIntegerExpression(int depth) {
        switch (Randomization.fromOptions(IntExpression.values())) {
            case UNARY_OPERATION:
                Neo4JExpression intExpression = generateExpression(depth + 1, Neo4JType.INTEGER);
                Neo4JPrefixOperation.PrefixOperator operator = Randomization.getBoolean()
                        ? Neo4JPrefixOperation.PrefixOperator.UNARY_PLUS
                        : Neo4JPrefixOperation.PrefixOperator.UNARY_MINUS;

                return new Neo4JPrefixOperation(intExpression, operator);
            case BINARY_ARITHMETIC_EXPRESSION:
                Neo4JExpression left = generateExpression(depth + 1, Neo4JType.INTEGER);
                Neo4JExpression right = generateExpression(depth + 1, Neo4JType.INTEGER);

                return new Neo4JBinaryArithmeticOperation(left, right, ArithmeticOperator.getRandomIntegerOperator());
            case FUNCTION:
                return generateFunction(depth + 1, Neo4JType.INTEGER);
            default:
                throw new AssertionError();
        }
    }

    private enum FloatExpression {
        UNARY_OPERATION, BINARY_ARITHMETIC_EXPRESSION, FUNCTION
    }

    private Neo4JExpression generateFloatExpression(int depth) {
        switch (Randomization.fromOptions(FloatExpression.values())) {
            case UNARY_OPERATION:
                Neo4JExpression intExpression = generateExpression(depth + 1, Neo4JType.FLOAT);
                Neo4JPrefixOperation.PrefixOperator operator = Randomization.getBoolean()
                        ? Neo4JPrefixOperation.PrefixOperator.UNARY_PLUS
                        : Neo4JPrefixOperation.PrefixOperator.UNARY_MINUS;

                return new Neo4JPrefixOperation(intExpression, operator);
            case BINARY_ARITHMETIC_EXPRESSION:
                Neo4JExpression left;
                Neo4JExpression right;

                // At least one of the two expressions has to be a float
                if (Randomization.getBoolean()) {
                    left = generateExpression(depth + 1, Neo4JType.FLOAT);
                    right = generateExpression(depth + 1, Neo4JType.FLOAT);
                } else {
                    if (Randomization.getBoolean()) {
                        left = generateExpression(depth + 1, Neo4JType.INTEGER);
                        right = generateExpression(depth + 1, Neo4JType.FLOAT);
                    } else {
                        left = generateExpression(depth + 1, Neo4JType.FLOAT);
                        right = generateExpression(depth + 1, Neo4JType.INTEGER);
                    }
                }

                return new Neo4JBinaryArithmeticOperation(left, right, ArithmeticOperator.getRandomFloatOperator());
            case FUNCTION:
                return generateFunction(depth + 1, Neo4JType.FLOAT);
            default:
                throw new AssertionError();
        }
    }

    private enum StringExpression {
        CONCAT, FUNCTION
    }

    private Neo4JExpression generateStringExpression(int depth) {
        switch (Randomization.fromOptions(StringExpression.values())) {
            case CONCAT:
                Neo4JExpression left = generateExpression(depth + 1, Neo4JType.STRING);
                Neo4JExpression right = generateExpression(depth + 1, Randomization.fromOptions(Neo4JType.STRING, Neo4JType.FLOAT, Neo4JType.INTEGER));
                return new Neo4JConcatOperation(left, right);
            case FUNCTION:
                return generateFunction(depth + 1, Neo4JType.STRING);
            default:
                throw new AssertionError();
        }
    }

    private Neo4JExpression generateDurationExpression(int depth) {
        return generateFunction(depth + 1, Neo4JType.DURATION);
    }

    public static Neo4JExpression generateExpression(Neo4JType type) {
        return new Neo4JExpressionGenerator().generateExpression(MAX_DEPTH, type);
    }

    private Neo4JExpression generateExpression(int depth, Neo4JType type) {
        return generateExpressionInternal(depth, type);
    }

    private Neo4JExpression generateExpressionInternal(int depth, Neo4JType type) {
        if (depth > MAX_DEPTH || Randomization.smallBiasProbability()) {
            return generateConstant(type);
        } else {
            switch (type) {
                case BOOLEAN:
                    return generateBooleanExpression(depth);
                case INTEGER:
                    return generateIntegerExpression(depth);
                case STRING:
                    return generateStringExpression(depth);
                case DURATION:
                    return generateDurationExpression(depth);
                case FLOAT:
                    return generateFloatExpression(depth);
                default:
                    return generateConstant(type);
            }
        }
    }

    private Neo4JFunctionCall generateFunction(int depth, Neo4JType returnType) {
        List<Neo4JFunctionCall.Neo4JFunction> functions = Stream.of(Neo4JFunctionCall.Neo4JFunction.values())
                .filter(neo4JFunction -> neo4JFunction.supportReturnType(returnType))
                .collect(Collectors.toList());

        if (functions.isEmpty()) {
            throw new IgnoreMeException();
        }

        Neo4JFunctionCall.Neo4JFunction chosenFunction = Randomization.fromList(functions);
        int arity = chosenFunction.getArity();
        Neo4JType[] argumentTypes = chosenFunction.getArgumentTypes(returnType);
        Neo4JExpression[] arguments = new Neo4JExpression[arity];

        for (int i = 0; i < arity; i++) {
            arguments[i] = generateExpression(depth + 1, argumentTypes[i]);
        }

        return new Neo4JFunctionCall(chosenFunction, arguments);
    }

}
