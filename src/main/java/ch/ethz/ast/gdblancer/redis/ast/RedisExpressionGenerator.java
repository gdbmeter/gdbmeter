package ch.ethz.ast.gdblancer.redis.ast;

import ch.ethz.ast.gdblancer.cypher.ast.*;
import ch.ethz.ast.gdblancer.cypher.ast.CypherBinaryArithmeticOperation.ArithmeticOperator;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherType;
import ch.ethz.ast.gdblancer.redis.RedisBugs;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RedisExpressionGenerator {

    public static final CypherType[] supportedTypes = {CypherType.INTEGER, CypherType.BOOLEAN, CypherType.FLOAT, CypherType.STRING, CypherType.POINT};
    private static final int MAX_DEPTH = 3;
    private final Map<String, CypherEntity> variables;

    public RedisExpressionGenerator(Map<String, CypherEntity> variables) {
        this.variables = variables;
    }

    public RedisExpressionGenerator() {
        this.variables = new HashMap<>();
    }

    public static CypherExpression generateConstant(CypherType type) {
        if (Randomization.smallBiasProbability()) {
            return new CypherConstant.NullConstant();
        }

        switch (type) {
            case INTEGER:
                return new CypherConstant.IntegerConstant(Randomization.getInteger());
            case BOOLEAN:
                return new CypherConstant.BooleanConstant(Randomization.getBoolean());
            case FLOAT:
                return new CypherConstant.FloatConstant(Randomization.nextFloat());
            case STRING:
                return new CypherConstant.StringConstant(Randomization.getString());
            case POINT:
                return new RedisPointConstant(
                        Randomization.nextDouble(-180, 180),
                        Randomization.nextDouble(-90, 90));
            default:
                throw new AssertionError(type);
        }
    }

    private enum BooleanExpression {
        BINARY_LOGICAL_OPERATOR, NOT,
        POSTFIX_OPERATOR, BINARY_COMPARISON,
        BINARY_STRING_OPERATOR, FUNCTION
    }

    // TODO: Support IN_OPERATION
    private CypherExpression generateBooleanExpression(int depth) {
        BooleanExpression option = Randomization.fromOptions(BooleanExpression.values());

        switch (option) {
            case BINARY_LOGICAL_OPERATOR:
                CypherExpression first = generateExpression(depth + 1, CypherType.BOOLEAN);
                int nr = Randomization.smallNumber() + 1;

                for (int i = 0; i < nr; i++) {
                    first = new CypherBinaryLogicalOperation(first,
                            generateExpression(depth + 1, CypherType.BOOLEAN),
                            CypherBinaryLogicalOperation.BinaryLogicalOperator.getRandom());
                }

                return first;
            case NOT:
                return new CypherPrefixOperation(generateExpression(depth + 1, CypherType.BOOLEAN),
                        CypherPrefixOperation.PrefixOperator.NOT);
            case POSTFIX_OPERATOR:
                return new CypherPostfixOperation(generateExpression(depth + 1),
                        CypherPostfixOperation.PostfixOperator.getRandom());
            case BINARY_COMPARISON:
                return generateComparison(depth, Randomization.fromOptions(CypherType.INTEGER, CypherType.BOOLEAN, CypherType.FLOAT, CypherType.STRING));
            case BINARY_STRING_OPERATOR:
                return new CypherBinaryStringOperation(generateExpression(depth + 1, CypherType.STRING),
                        generateExpression(depth + 1, CypherType.STRING),
                        CypherBinaryStringOperation.BinaryStringOperation.getRandom());
            case FUNCTION:
                return generateFunction(depth + 1, CypherType.BOOLEAN);
            default:
                throw new AssertionError(option);
        }
    }

    private CypherExpression generateComparison(int depth, CypherType type) {
        CypherExpression left = generateExpression(depth + 1, type);
        CypherExpression right = generateExpression(depth + 1, type);
        return new CypherBinaryComparisonOperation(left, right,
                CypherBinaryComparisonOperation.BinaryComparisonOperator.getRandom());
    }

    private enum IntExpression {
        UNARY_OPERATION, BINARY_ARITHMETIC_EXPRESSION, FUNCTION
    }

    private CypherExpression generateIntegerExpression(int depth) {
        switch (Randomization.fromOptions(IntExpression.values())) {
            case UNARY_OPERATION:
                CypherExpression intExpression = generateExpression(depth + 1, CypherType.INTEGER);
                CypherPrefixOperation.PrefixOperator operator = Randomization.getBoolean()
                        ? CypherPrefixOperation.PrefixOperator.UNARY_PLUS
                        : CypherPrefixOperation.PrefixOperator.UNARY_MINUS;

                return new CypherPrefixOperation(intExpression, operator);
            case BINARY_ARITHMETIC_EXPRESSION:
                CypherExpression left = generateExpression(depth + 1, CypherType.INTEGER);
                CypherExpression right = generateExpression(depth + 1, CypherType.INTEGER);

                ArithmeticOperator binaryOperator;
                if (RedisBugs.bug2377) {
                    binaryOperator = Randomization.fromOptions(ArithmeticOperator.ADDITION,
                            ArithmeticOperator.SUBTRACTION, ArithmeticOperator.MULTIPLICATION,
                            ArithmeticOperator.DIVISION);
                } else {
                    // TODO: DO not allow division -> float ?
                    binaryOperator = ArithmeticOperator.getRandomIntegerOperator();
                }

                return new CypherBinaryArithmeticOperation(left, right, binaryOperator);
            case FUNCTION:
                return generateFunction(depth + 1, CypherType.INTEGER);
            default:
                throw new AssertionError();
        }
    }

    private enum FloatExpression {
        UNARY_OPERATION, BINARY_ARITHMETIC_EXPRESSION, FUNCTION
    }

    private CypherExpression generateFloatExpression(int depth) {
        switch (Randomization.fromOptions(FloatExpression.values())) {
            case UNARY_OPERATION:
                CypherExpression intExpression = generateExpression(depth + 1, CypherType.FLOAT);
                CypherPrefixOperation.PrefixOperator unaryOperator = Randomization.getBoolean()
                        ? CypherPrefixOperation.PrefixOperator.UNARY_PLUS
                        : CypherPrefixOperation.PrefixOperator.UNARY_MINUS;

                return new CypherPrefixOperation(intExpression, unaryOperator);
            case BINARY_ARITHMETIC_EXPRESSION:
                CypherExpression left;
                CypherExpression right;

                // At least one of the two expressions has to be a float
                if (Randomization.getBoolean()) {
                    left = generateExpression(depth + 1, CypherType.FLOAT);
                    right = generateExpression(depth + 1, CypherType.FLOAT);
                } else {
                    if (Randomization.getBoolean()) {
                        left = generateExpression(depth + 1, CypherType.INTEGER);
                        right = generateExpression(depth + 1, CypherType.FLOAT);
                    } else {
                        left = generateExpression(depth + 1, CypherType.FLOAT);
                        right = generateExpression(depth + 1, CypherType.INTEGER);
                    }
                }

                ArithmeticOperator binaryOperator;

                if (RedisBugs.bug2377) {
                    binaryOperator = Randomization.fromOptions(ArithmeticOperator.ADDITION,
                            ArithmeticOperator.SUBTRACTION, ArithmeticOperator.MULTIPLICATION,
                            ArithmeticOperator.DIVISION, ArithmeticOperator.EXPONENTIATION
                    );
                } else {
                    binaryOperator = ArithmeticOperator.getRandomFloatOperator();
                }

                return new CypherBinaryArithmeticOperation(left, right, binaryOperator);
            case FUNCTION:
                return generateFunction(depth + 1, CypherType.FLOAT);
            default:
                throw new AssertionError();
        }
    }

    private enum StringExpression {
        CONCAT, FUNCTION
    }

    private CypherExpression generateStringExpression(int depth) {
        switch (Randomization.fromOptions(StringExpression.values())) {
            case CONCAT:
                CypherExpression left = generateExpression(depth + 1, CypherType.STRING);
                CypherExpression right = generateExpression(depth + 1, Randomization.fromOptions(CypherType.STRING, CypherType.FLOAT, CypherType.INTEGER));
                return new CypherConcatOperation(left, right);
            case FUNCTION:
                return generateFunction(depth + 1, CypherType.STRING);
            default:
                throw new AssertionError();
        }
    }

    public static CypherExpression generateExpression() {
        return generateExpression(Randomization.fromOptions(supportedTypes));
    }

    public static CypherExpression generateExpression(CypherType type) {
        return new RedisExpressionGenerator().generateExpression(0, type);
    }

    public CypherExpression generateExpression(int depth) {
        return generateExpression(depth, Randomization.fromOptions(supportedTypes));
    }

    private CypherExpression generateExpression(int depth, CypherType type) {
        if (!filterVariables(type).isEmpty() && Randomization.getBoolean()) {
            return getVariableExpression(type);
        }

        return generateExpressionInternal(depth, type);
    }

    public static CypherExpression generateExpression(Map<String, CypherEntity> variables, CypherType type) {
        return new RedisExpressionGenerator(variables).generateExpression(0, type);
    }

    public static CypherExpression generateExpression(Map<String, CypherEntity> variables) {
        return generateExpression(variables, Randomization.fromOptions(supportedTypes));
    }

    private CypherExpression getVariableExpression(CypherType type) {
        List<String> variables = filterVariables(type);
        return new CypherVariablePropertyAccess(Randomization.fromList(variables));
    }

    private List<String> filterVariables(CypherType type) {
        if (variables == null) {
            return Collections.emptyList();
        } else {
            List<String> filteredVariables = new ArrayList<>();

            for (String variable : variables.keySet()) {
                Map<String, CypherType> properties = variables.get(variable).getAvailableProperties();

                for (String property : properties.keySet()) {
                    if (properties.get(property) == type) {
                        filteredVariables.add(variable + "." + property);
                    }
                }
            }

            return filteredVariables;
        }
    }

    private CypherExpression generateExpressionInternal(int depth, CypherType type) {
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
                case FLOAT:
                    return generateFloatExpression(depth);
                default:
                    return generateConstant(type);
            }
        }
    }

    private CypherFunctionCall generateFunction(int depth, CypherType returnType) {
        List<RedisFunction> functions = Stream.of(RedisFunction.values())
                .filter(neo4JFunction -> neo4JFunction.supportReturnType(returnType))
                .collect(Collectors.toList());

        if (RedisBugs.bug2374) {
            functions.remove(RedisFunction.SUBSTRING);
        }

        if (RedisBugs.bug2375) {
            functions.remove(RedisFunction.RIGHT);
        }

        if (RedisBugs.bug2382) {
            functions.remove(RedisFunction.RIGHT);
            functions.remove(RedisFunction.LEFT);
        }

        if (functions.isEmpty()) {
            throw new IgnoreMeException();
        }

        RedisFunction chosenFunction = Randomization.fromList(functions);
        int arity = chosenFunction.getArity();
        CypherType[] argumentTypes = chosenFunction.getArgumentTypes(returnType);
        CypherExpression[] arguments = new CypherExpression[arity];

        for (int i = 0; i < arity; i++) {
            arguments[i] = generateExpression(depth + 1, argumentTypes[i]);
        }

        return new CypherFunctionCall(chosenFunction, arguments);
    }

}
