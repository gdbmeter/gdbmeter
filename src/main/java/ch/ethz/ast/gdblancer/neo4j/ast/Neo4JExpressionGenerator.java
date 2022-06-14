package ch.ethz.ast.gdblancer.neo4j.ast;

import ch.ethz.ast.gdblancer.cypher.ast.*;
import ch.ethz.ast.gdblancer.neo4j.Neo4JBugs;
import ch.ethz.ast.gdblancer.cypher.ast.CypherBinaryArithmeticOperation.ArithmeticOperator;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherType;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Neo4JExpressionGenerator {

    private static final int MAX_DEPTH = 3;

    private final Map<String, CypherEntity> variables;

    public Neo4JExpressionGenerator(Map<String, CypherEntity> variables) {
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

    public static CypherExpression generateConstant(CypherType type) {
        if (Randomization.smallBiasProbability()) {
            return new CypherConstant.NullConstant();
        }

        switch (type) {
            case INTEGER:
                switch (Randomization.fromOptions(IntegerConstantFormat.values())) {
                    case NORMAL_INTEGER:
                        return new CypherConstant.IntegerConstant(Randomization.getInteger());
                    case HEX_INTEGER:
                        return new CypherConstant.IntegerHexConstant(Randomization.getInteger());
                    case OCTAL_INTEGER:
                        return new CypherConstant.IntegerOctalConstant(Randomization.getInteger(), CypherConstant.IntegerOctalConstant.OctalPrefix.getRandom());
                }
            case BOOLEAN:
                return new CypherConstant.BooleanConstant(Randomization.getBoolean());
            case FLOAT:
                return new CypherConstant.FloatConstant(Randomization.nextFloat());
            case STRING:
                return new CypherConstant.StringConstant(Randomization.getString());
            case POINT:
                if (Randomization.getBoolean()) {
                    return new CypherConstant.PointConstant(Randomization.getDouble(), Randomization.getDouble());
                } else {
                    return new CypherConstant.PointConstant(Randomization.getDouble(), Randomization.getDouble(), Randomization.getDouble());
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

                return new CypherConstant.DurationConstant(datePart, timePart);
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

                return new CypherConstant.LocalTimeConstant(hours,
                        separator,
                        minutes,
                        seconds,
                        nanoSecondSeparator,
                        nanoSeconds);
            case DATE:
                return new CypherConstant.DateConstant(Randomization.getBoolean(),
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
                if (Neo4JBugs.PartitionOracleSpecific.bug12884) {
                    return generateComparison(depth, Randomization.fromOptions(CypherType.INTEGER,
                            CypherType.FLOAT, CypherType.STRING, CypherType.BOOLEAN, CypherType.DATE,
                            CypherType.LOCAL_TIME, CypherType.POINT));
                } else {
                    return generateComparison(depth, Randomization.fromOptions(CypherType.values()));
                }

            case BINARY_STRING_OPERATOR:
                return new CypherBinaryStringOperation(generateExpression(depth + 1, CypherType.STRING),
                        generateExpression(depth + 1, CypherType.STRING),
                        CypherBinaryStringOperation.BinaryStringOperation.getRandom());
            case REGEX:
                return new CypherRegularExpression(generateExpression(depth + 1, CypherType.STRING),
                        generateExpression(depth + 1, CypherType.STRING));
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

                if (Neo4JBugs.PartitionOracleSpecific.bug12883) {
                    return new CypherBinaryArithmeticOperation(left, right, ArithmeticOperator.getRandomIntegerOperatorNaNSafe());
                } else {
                    return new CypherBinaryArithmeticOperation(left, right, ArithmeticOperator.getRandomIntegerOperator());
                }
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
                CypherPrefixOperation.PrefixOperator operator = Randomization.getBoolean()
                        ? CypherPrefixOperation.PrefixOperator.UNARY_PLUS
                        : CypherPrefixOperation.PrefixOperator.UNARY_MINUS;

                return new CypherPrefixOperation(intExpression, operator);
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

                if (Neo4JBugs.PartitionOracleSpecific.bug12883) {
                    return new CypherBinaryArithmeticOperation(left, right, ArithmeticOperator.getRandomFloatOperatorNaNSafe());
                } else {
                    return new CypherBinaryArithmeticOperation(left, right, ArithmeticOperator.getRandomFloatOperator());
                }

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

    private CypherExpression generateDurationExpression(int depth) {
        return generateFunction(depth + 1, CypherType.DURATION);
    }

    public static CypherExpression generateExpression() {
        return generateExpression(CypherType.getRandom());
    }

    public static CypherExpression generateExpression(CypherType type) {
        return new Neo4JExpressionGenerator().generateExpression(0, type);
    }

    public CypherExpression generateExpression(int depth) {
        return generateExpression(depth, CypherType.getRandom());
    }

    private CypherExpression generateExpression(int depth, CypherType type) {
        if (!filterVariables(type).isEmpty() && Randomization.getBoolean()) {
            return getVariableExpression(type);
        }

        return generateExpressionInternal(depth, type);
    }

    public static CypherExpression generateExpression(Map<String, CypherEntity> variables, CypherType type) {
        return new Neo4JExpressionGenerator(variables).generateExpression(0, type);
    }

    public static CypherExpression generateExpression(Map<String, CypherEntity> variables) {
        return generateExpression(variables, CypherType.getRandom());
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
                case DURATION:
                    return generateDurationExpression(depth);
                case FLOAT:
                    return generateFloatExpression(depth);
                default:
                    return generateConstant(type);
            }
        }
    }

    private CypherFunctionCall generateFunction(int depth, CypherType returnType) {
        List<Neo4JFunction> functions = Stream.of(Neo4JFunction.values())
                .filter(neo4JFunction -> neo4JFunction.supportReturnType(returnType))
                .collect(Collectors.toList());

        if (Neo4JBugs.PartitionOracleSpecific.bug12887) {
            functions.remove(Neo4JFunction.LTRIM);
        }

        if (Neo4JBugs.PartitionOracleSpecific.bug12883) {
            functions.remove(Neo4JFunction.SQRT);
            functions.remove(Neo4JFunction.LOG);
            functions.remove(Neo4JFunction.LOG_10);
            functions.remove(Neo4JFunction.EXP); // removed so that no infinity is generated
        }

        if (functions.isEmpty()) {
            throw new IgnoreMeException();
        }

        Neo4JFunction chosenFunction = Randomization.fromList(functions);
        int arity = chosenFunction.getArity();
        CypherType[] argumentTypes = chosenFunction.getArgumentTypes(returnType);
        CypherExpression[] arguments = new CypherExpression[arity];

        for (int i = 0; i < arity; i++) {
            arguments[i] = generateExpression(depth + 1, argumentTypes[i]);
        }

        return new CypherFunctionCall(chosenFunction, arguments);
    }

}
