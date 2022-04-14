package ch.ethz.ast.gdblancer.neo4j.gen.ast;

import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBPropertyType;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.LinkedHashMap;
import java.util.Map;

public class Neo4JExpressionGenerator {

    public static Neo4JExpression generateConstant(Neo4JDBPropertyType type) {
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

}
