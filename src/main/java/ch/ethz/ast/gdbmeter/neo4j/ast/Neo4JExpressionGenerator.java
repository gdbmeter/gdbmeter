package ch.ethz.ast.gdbmeter.neo4j.ast;

import ch.ethz.ast.gdbmeter.cypher.ast.*;
import ch.ethz.ast.gdbmeter.neo4j.Neo4JBugs;
import ch.ethz.ast.gdbmeter.cypher.ast.CypherBinaryArithmeticOperation.ArithmeticOperator;
import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.neo4j.schema.Neo4JType;
import ch.ethz.ast.gdbmeter.util.IgnoreMeException;
import ch.ethz.ast.gdbmeter.util.Randomization;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Neo4JExpressionGenerator {

    private static final int MAX_DEPTH = 3;

    private final Map<String, Entity<Neo4JType>> variables;

    public Neo4JExpressionGenerator(Map<String, Entity<Neo4JType>> variables) {
        this.variables = variables;
    }

    public Neo4JExpressionGenerator() {
        this.variables = new HashMap<>();
    }

    private enum IntegerConstantFormat {
        NORMAL_INTEGER,
        HEX_INTEGER,
        OCTAL_INTEGER
    }

    public static CypherExpression generateConstant(Neo4JType type) {
        if (Randomization.smallBiasProbability()) {
            return new CypherConstant.NullConstant();
        }

        switch (type) {
            case INTEGER:
                return switch (Randomization.fromOptions(IntegerConstantFormat.values())) {
                    case NORMAL_INTEGER -> new CypherConstant.IntegerConstant(Randomization.getInteger());
                    case HEX_INTEGER -> new CypherConstant.IntegerHexConstant(Randomization.getInteger());
                    case OCTAL_INTEGER -> new CypherConstant.IntegerOctalConstant(Randomization.getInteger());
                };
            case BOOLEAN:
                return new CypherConstant.BooleanConstant(Randomization.getBoolean());
            case FLOAT:
                return new CypherConstant.FloatConstant(Randomization.getFloat());
            case STRING:
                return new CypherConstant.StringConstant(Randomization.getString());
            case POINT:
                if (Randomization.getBoolean()) {
                    return new Neo4JPointConstant(Randomization.getDouble(), Randomization.getDouble());
                } else {
                    return new Neo4JPointConstant(Randomization.getDouble(), Randomization.getDouble(), Randomization.getDouble());
                }
            case DURATION:
                Map<String, Long> datePart;
                Map<String, Long> timePart;

                do {
                    datePart = new LinkedHashMap<>();

                    for (String current : new String[]{"Y", "M", "W", "D"}) {
                        if (Randomization.getBoolean()) {
                            if (Neo4JBugs.bug12861) {
                                datePart.put(current, Randomization.getPositiveInt());
                            } else {
                                datePart.put(current, Randomization.getPositiveInteger());
                            }
                        }
                    }

                    timePart = new LinkedHashMap<>();

                    for (String current : new String[]{"H", "M", "S"}) {
                        if (Randomization.getBoolean()) {
                            if (Neo4JBugs.bug12861) {
                                timePart.put(current, Randomization.getPositiveInt());
                            } else {
                                timePart.put(current, Randomization.getPositiveInteger());
                            }
                        }
                    }
                } while (datePart.isEmpty() && timePart.isEmpty());

                return new Neo4JDurationConstant(datePart, timePart);
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

                return new Neo4JLocalTimeConstant(hours,
                        separator,
                        minutes,
                        seconds,
                        nanoSecondSeparator,
                        nanoSeconds);
            case DATE:
                return new Neo4JDateConstant(Randomization.getBoolean(),
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
    private CypherExpression generateBooleanExpression(int depth) {
        BooleanExpression option = Randomization.fromOptions(BooleanExpression.values());

        switch (option) {
            case BINARY_LOGICAL_OPERATOR:
                CypherExpression first = generateExpression(depth + 1, Neo4JType.BOOLEAN);
                int nr = Randomization.smallNumber() + 1;

                for (int i = 0; i < nr; i++) {
                    first = new CypherBinaryLogicalOperation(first,
                            generateExpression(depth + 1, Neo4JType.BOOLEAN),
                            CypherBinaryLogicalOperation.BinaryLogicalOperator.getRandom());
                }

                return first;
            case NOT:
                return new CypherPrefixOperation(generateExpression(depth + 1, Neo4JType.BOOLEAN),
                        CypherPrefixOperation.PrefixOperator.NOT);
            case POSTFIX_OPERATOR:
                return new CypherPostfixOperation(generateExpression(depth + 1),
                        CypherPostfixOperation.PostfixOperator.getRandom());
            case BINARY_COMPARISON:
                if (Neo4JBugs.PartitionOracleSpecific.bug12884) {
                    return generateComparison(depth, Randomization.fromOptions(Neo4JType.INTEGER,
                            Neo4JType.FLOAT, Neo4JType.STRING, Neo4JType.BOOLEAN, Neo4JType.DATE,
                            Neo4JType.LOCAL_TIME, Neo4JType.POINT));
                } else {
                    return generateComparison(depth, Randomization.fromOptions(Neo4JType.values()));
                }

            case BINARY_STRING_OPERATOR:
                return new CypherBinaryStringOperation(generateExpression(depth + 1, Neo4JType.STRING),
                        generateExpression(depth + 1, Neo4JType.STRING),
                        CypherBinaryStringOperation.BinaryStringOperation.getRandom());
            case REGEX:
                return new CypherRegularExpression(generateExpression(depth + 1, Neo4JType.STRING),
                        generateExpression(depth + 1, Neo4JType.STRING));
            case FUNCTION:
                return generateFunction(depth + 1, Neo4JType.BOOLEAN);
            default:
                throw new AssertionError(option);
        }
    }

    private CypherExpression generateComparison(int depth, Neo4JType type) {
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
                CypherExpression intExpression = generateExpression(depth + 1, Neo4JType.INTEGER);
                CypherPrefixOperation.PrefixOperator operator = Randomization.getBoolean()
                        ? CypherPrefixOperation.PrefixOperator.UNARY_PLUS
                        : CypherPrefixOperation.PrefixOperator.UNARY_MINUS;

                return new CypherPrefixOperation(intExpression, operator);
            case BINARY_ARITHMETIC_EXPRESSION:
                CypherExpression left = generateExpression(depth + 1, Neo4JType.INTEGER);
                CypherExpression right = generateExpression(depth + 1, Neo4JType.INTEGER);

                return new CypherBinaryArithmeticOperation(left, right, ArithmeticOperator.getRandomIntegerOperator());
            case FUNCTION:
                return generateFunction(depth + 1, Neo4JType.INTEGER);
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
                CypherExpression intExpression = generateExpression(depth + 1, Neo4JType.FLOAT);
                CypherPrefixOperation.PrefixOperator operator = Randomization.getBoolean()
                        ? CypherPrefixOperation.PrefixOperator.UNARY_PLUS
                        : CypherPrefixOperation.PrefixOperator.UNARY_MINUS;

                return new CypherPrefixOperation(intExpression, operator);
            case BINARY_ARITHMETIC_EXPRESSION:
                CypherExpression left;
                CypherExpression right;

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

                return new CypherBinaryArithmeticOperation(left, right, ArithmeticOperator.getRandomFloatOperator());
            case FUNCTION:
                return generateFunction(depth + 1, Neo4JType.FLOAT);
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
                CypherExpression left = generateExpression(depth + 1, Neo4JType.STRING);
                CypherExpression right = generateExpression(depth + 1, Randomization.fromOptions(Neo4JType.STRING, Neo4JType.FLOAT, Neo4JType.INTEGER));
                return new CypherConcatOperation(left, right);
            case FUNCTION:
                return generateFunction(depth + 1, Neo4JType.STRING);
            default:
                throw new AssertionError();
        }
    }

    private CypherExpression generateDurationExpression(int depth) {
        return generateFunction(depth + 1, Neo4JType.DURATION);
    }

    public static CypherExpression generateExpression() {
        return generateExpression(Neo4JType.getRandom());
    }

    public static CypherExpression generateExpression(Neo4JType type) {
        return new Neo4JExpressionGenerator().generateExpression(0, type);
    }

    public CypherExpression generateExpression(int depth) {
        return generateExpression(depth, Neo4JType.getRandom());
    }

    private CypherExpression generateExpression(int depth, Neo4JType type) {
        if (!filterVariables(type).isEmpty() && Randomization.getBoolean()) {
            return getVariableExpression(type);
        }

        return generateExpressionInternal(depth, type);
    }

    public static CypherExpression generateExpression(Map<String, Entity<Neo4JType>> variables, Neo4JType type) {
        return new Neo4JExpressionGenerator(variables).generateExpression(0, type);
    }

    public static CypherExpression generateExpression(Map<String, Entity<Neo4JType>> variables) {
        return generateExpression(variables, Neo4JType.getRandom());
    }

    private CypherExpression getVariableExpression(Neo4JType type) {
        List<String> variables = filterVariables(type);
        return new CypherVariablePropertyAccess(Randomization.fromList(variables));
    }

    private List<String> filterVariables(Neo4JType type) {
        if (variables == null) {
            return Collections.emptyList();
        } else {
            List<String> filteredVariables = new ArrayList<>();

            for (String variable : variables.keySet()) {
                Map<String, Neo4JType> properties = variables.get(variable).availableProperties();

                for (String property : properties.keySet()) {
                    if (properties.get(property) == type) {
                        filteredVariables.add(variable + "." + property);
                    }
                }
            }

            return filteredVariables;
        }
    }

    private CypherExpression generateExpressionInternal(int depth, Neo4JType type) {
        if (depth > MAX_DEPTH || Randomization.smallBiasProbability()) {
            return generateConstant(type);
        } else {
            return switch (type) {
                case BOOLEAN -> generateBooleanExpression(depth);
                case INTEGER -> generateIntegerExpression(depth);
                case STRING -> generateStringExpression(depth);
                case DURATION -> generateDurationExpression(depth);
                case FLOAT -> generateFloatExpression(depth);
                default -> generateConstant(type);
            };
        }
    }

    private CypherFunctionCall<Neo4JType> generateFunction(int depth, Neo4JType returnType) {
        List<Neo4JFunction> functions = Stream.of(Neo4JFunction.values())
                .filter(function -> function.supportReturnType(returnType))
                .collect(Collectors.toList());

        if (functions.isEmpty()) {
            throw new IgnoreMeException();
        }

        Neo4JFunction chosenFunction = Randomization.fromList(functions);
        int arity = chosenFunction.getArity();
        Neo4JType[] argumentTypes = chosenFunction.getArgumentTypes(returnType);
        CypherExpression[] arguments = new CypherExpression[arity];

        for (int i = 0; i < arity; i++) {
            arguments[i] = generateExpression(depth + 1, argumentTypes[i]);
        }

        return new CypherFunctionCall<>(chosenFunction, arguments);
    }

}
