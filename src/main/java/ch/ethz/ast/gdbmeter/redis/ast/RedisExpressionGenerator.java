package ch.ethz.ast.gdbmeter.redis.ast;

import ch.ethz.ast.gdbmeter.cypher.ast.*;
import ch.ethz.ast.gdbmeter.cypher.ast.CypherBinaryArithmeticOperation.ArithmeticOperator;
import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.redis.RedisBugs;
import ch.ethz.ast.gdbmeter.redis.schema.RedisType;
import ch.ethz.ast.gdbmeter.util.IgnoreMeException;
import ch.ethz.ast.gdbmeter.util.Randomization;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: Try to extract common base class from Neo4JExpressionGenerator
public class RedisExpressionGenerator {
    
    private static final int MAX_DEPTH = 3;
    private final Map<String, Entity<RedisType>> variables;

    public RedisExpressionGenerator(Map<String, Entity<RedisType>> variables) {
        this.variables = variables;
    }

    public RedisExpressionGenerator() {
        this.variables = new HashMap<>();
    }

    public static CypherExpression generateConstant(RedisType type) {
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
                CypherExpression first = generateExpression(depth + 1, RedisType.BOOLEAN);
                int nr = Randomization.smallNumber() + 1;

                for (int i = 0; i < nr; i++) {
                    first = new CypherBinaryLogicalOperation(first,
                            generateExpression(depth + 1, RedisType.BOOLEAN),
                            CypherBinaryLogicalOperation.BinaryLogicalOperator.getRandom());
                }

                return first;
            case NOT:
                return new CypherPrefixOperation(generateExpression(depth + 1, RedisType.BOOLEAN),
                        CypherPrefixOperation.PrefixOperator.NOT);
            case POSTFIX_OPERATOR:
                return new CypherPostfixOperation(generateExpression(depth + 1),
                        CypherPostfixOperation.PostfixOperator.getRandom());
            case BINARY_COMPARISON:
                return generateComparison(depth, Randomization.fromOptions(RedisType.INTEGER, RedisType.BOOLEAN, RedisType.FLOAT, RedisType.STRING));
            case BINARY_STRING_OPERATOR:
                return new CypherBinaryStringOperation(generateExpression(depth + 1, RedisType.STRING),
                        generateExpression(depth + 1, RedisType.STRING),
                        CypherBinaryStringOperation.BinaryStringOperation.getRandom());
            case FUNCTION:
                return generateFunction(depth + 1, RedisType.BOOLEAN);
            default:
                throw new AssertionError(option);
        }
    }

    private CypherExpression generateComparison(int depth, RedisType type) {
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
                CypherExpression intExpression = generateExpression(depth + 1, RedisType.INTEGER);
                CypherPrefixOperation.PrefixOperator unaryOperator = Randomization.getBoolean()
                        ? CypherPrefixOperation.PrefixOperator.UNARY_PLUS
                        : CypherPrefixOperation.PrefixOperator.UNARY_MINUS;

                return new CypherPrefixOperation(intExpression, unaryOperator);
            case BINARY_ARITHMETIC_EXPRESSION:
                CypherExpression left = generateExpression(depth + 1, RedisType.INTEGER);
                CypherExpression right = generateExpression(depth + 1, RedisType.INTEGER);

                // Note that division is not supported since it generates a float value in RedisGraph!
                ArithmeticOperator binaryOperator = Randomization.fromOptions(ArithmeticOperator.ADDITION,
                        ArithmeticOperator.SUBTRACTION,
                        ArithmeticOperator.MULTIPLICATION,
                        ArithmeticOperator.MODULO);
                return new CypherBinaryArithmeticOperation(left, right, binaryOperator);
            case FUNCTION:
                return generateFunction(depth + 1, RedisType.INTEGER);
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
                CypherExpression intExpression = generateExpression(depth + 1, RedisType.FLOAT);
                CypherPrefixOperation.PrefixOperator unaryOperator = Randomization.getBoolean()
                        ? CypherPrefixOperation.PrefixOperator.UNARY_PLUS
                        : CypherPrefixOperation.PrefixOperator.UNARY_MINUS;

                return new CypherPrefixOperation(intExpression, unaryOperator);
            case BINARY_ARITHMETIC_EXPRESSION:
                CypherExpression left;
                CypherExpression right;

                // At least one of the two expressions has to be a float
                if (Randomization.getBoolean()) {
                    left = generateExpression(depth + 1, RedisType.FLOAT);
                    right = generateExpression(depth + 1, RedisType.FLOAT);
                } else {
                    if (Randomization.getBoolean()) {
                        left = generateExpression(depth + 1, RedisType.INTEGER);
                        right = generateExpression(depth + 1, RedisType.FLOAT);
                    } else {
                        left = generateExpression(depth + 1, RedisType.FLOAT);
                        right = generateExpression(depth + 1, RedisType.INTEGER);
                    }
                }

                return new CypherBinaryArithmeticOperation(left, right, ArithmeticOperator.getRandomFloatOperator());
            case FUNCTION:
                return generateFunction(depth + 1, RedisType.FLOAT);
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
                CypherExpression left = generateExpression(depth + 1, RedisType.STRING);
                CypherExpression right = generateExpression(depth + 1, Randomization.fromOptions(RedisType.STRING, RedisType.FLOAT, RedisType.INTEGER));
                return new CypherConcatOperation(left, right);
            case FUNCTION:
                return generateFunction(depth + 1, RedisType.STRING);
            default:
                throw new AssertionError();
        }
    }

    public static CypherExpression generateExpression() {
        return generateExpression(RedisType.getRandom());
    }

    public static CypherExpression generateExpression(RedisType type) {
        return new RedisExpressionGenerator().generateExpression(0, type);
    }

    public CypherExpression generateExpression(int depth) {
        return generateExpression(depth, RedisType.getRandom());
    }

    private CypherExpression generateExpression(int depth, RedisType type) {
        if (!filterVariables(type).isEmpty() && Randomization.getBoolean()) {
            return getVariableExpression(type);
        }

        return generateExpressionInternal(depth, type);
    }

    public static CypherExpression generateExpression(Map<String, Entity<RedisType>> variables, RedisType type) {
        return new RedisExpressionGenerator(variables).generateExpression(0, type);
    }

    public static CypherExpression generateExpression(Map<String, Entity<RedisType>> variables) {
        return generateExpression(variables, RedisType.getRandom());
    }

    private CypherExpression getVariableExpression(RedisType type) {
        List<String> variables = filterVariables(type);
        return new CypherVariablePropertyAccess(Randomization.fromList(variables));
    }

    private List<String> filterVariables(RedisType type) {
        if (variables == null) {
            return Collections.emptyList();
        } else {
            List<String> filteredVariables = new ArrayList<>();

            for (String variable : variables.keySet()) {
                Map<String, RedisType> properties = variables.get(variable).getAvailableProperties();

                for (String property : properties.keySet()) {
                    if (properties.get(property) == type) {
                        filteredVariables.add(variable + "." + property);
                    }
                }
            }

            return filteredVariables;
        }
    }

    private CypherExpression generateExpressionInternal(int depth, RedisType type) {
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

    private CypherFunctionCall<RedisType> generateFunction(int depth, RedisType returnType) {
        List<RedisFunction> functions = Stream.of(RedisFunction.values())
                .filter(function -> function.supportReturnType(returnType))
                .collect(Collectors.toList());

        if (functions.isEmpty()) {
            throw new IgnoreMeException();
        }

        if (RedisBugs.bug2433) {
            functions.remove(RedisFunction.POINT_DISTANCE);
        }

        RedisFunction chosenFunction = Randomization.fromList(functions);
        int arity = chosenFunction.getArity();
        RedisType[] argumentTypes = chosenFunction.getArgumentTypes(returnType);
        CypherExpression[] arguments = new CypherExpression[arity];

        for (int i = 0; i < arity; i++) {
            arguments[i] = generateExpression(depth + 1, argumentTypes[i]);
        }

        return new CypherFunctionCall<>(chosenFunction, arguments);
    }

}
