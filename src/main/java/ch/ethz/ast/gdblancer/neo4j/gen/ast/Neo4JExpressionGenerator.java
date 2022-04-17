package ch.ethz.ast.gdblancer.neo4j.gen.ast;

import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JType;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.LinkedHashMap;
import java.util.Map;

public class Neo4JExpressionGenerator {

    private static final int MAX_DEPTH = 3;

    public static Neo4JExpression generateConstant(Neo4JType type) {
        if (Randomization.smallBiasProbability()) {
            return new Neo4JConstant.NullConstant();
        }

        switch (type) {
            case INTEGER:
                return new Neo4JConstant.IntegerConstant((int) Randomization.getInteger());
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
        BINARY_LOGICAL_OPERATOR, NOT, POSTFIX_OPERATOR, BINARY_COMPARISON, STRING_STRING_OPERATOR, REGEX
    }

    // TODO: Support IN_OPERATION
    private static Neo4JExpression generateBooleanExpression(int depth) {
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
            case STRING_STRING_OPERATOR:
                return new Neo4JBinaryStringOperation(generateExpression(depth + 1, Neo4JType.STRING),
                        generateExpression(depth + 1, Neo4JType.STRING),
                        Neo4JBinaryStringOperation.BinaryStringOperation.getRandom());
            case REGEX:
                return new Neo4JRegularExpression(generateExpression(depth + 1, Neo4JType.STRING),
                        generateExpression(depth + 1, Neo4JType.STRING));
            default:
                throw new AssertionError(option);
        }
    }

    private static Neo4JExpression generateComparison(int depth, Neo4JType type) {
        Neo4JExpression left = generateExpression(depth + 1, type);
        Neo4JExpression right = generateExpression(depth + 1, type);
        return new Neo4JBinaryComparisonOperation(left, right,
                Neo4JBinaryComparisonOperation.BinaryComparisonOperator.getRandom());
    }

    public static Neo4JExpression generateExpression(int depth, Neo4JType type) {
        if (depth > MAX_DEPTH || Randomization.smallBiasProbability()) {
            return generateConstant(type);
        } else {
            if (type == Neo4JType.BOOLEAN) {
                return generateBooleanExpression(depth);
            } else {
                return generateConstant(type);
            }
        }
    }

}
