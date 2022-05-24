package ch.ethz.ast.gdblancer.neo4j.gen.ast;

import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JType;
import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JFunctionCall implements Neo4JExpression {

    private final Neo4JFunction function;
    private final Neo4JExpression[] arguments;

    public Neo4JFunctionCall(Neo4JFunction function,
                             Neo4JExpression[] arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    public String getFunctionName() {
        return function.getName();
    }

    public Neo4JExpression[] getArguments() {
        return arguments;
    }

    public enum Neo4JFunction {

        TO_BOOLEAN("toBoolean", 1) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.BOOLEAN;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                Neo4JType chosenType = Randomization.fromOptions(Neo4JType.BOOLEAN,
                        Neo4JType.STRING,
                        Neo4JType.INTEGER);

                return new Neo4JType[] { chosenType };
            }
        },
        TO_BOOLEAN_OR_NULL("toBooleanOrNull" , 1) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.BOOLEAN;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[]{Neo4JType.getRandom()};
            }
        },
        ABS("abs", 1) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.INTEGER || returnType == Neo4JType.FLOAT;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                assert returnType == Neo4JType.INTEGER || returnType == Neo4JType.FLOAT;
                return new Neo4JType[] { returnType };
            }
        },
        SIGN("sign", 1) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.INTEGER;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Randomization.fromOptions(Neo4JType.INTEGER, Neo4JType.FLOAT) };
            }
        },
        TO_INTEGER("toInteger", 1) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.INTEGER;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                Neo4JType chosenType = Randomization.fromOptions(Neo4JType.BOOLEAN,
                        Neo4JType.STRING,
                        Neo4JType.INTEGER,
                        Neo4JType.FLOAT);

                return new Neo4JType[] { chosenType };
            }
        },
        TO_INTEGER_OR_NULL("toIntegerOrNull", 1) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.INTEGER;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.getRandom() };
            }
        },
        DURATION_BETWEEN("duration.between", 2) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.DURATION;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.DATE, Neo4JType.DATE };
            }
        },
        DURATION_IN_MONTHS("duration.inMonths", 2) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.DURATION;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.DATE, Neo4JType.DATE };
            }
        },
        DURATION_IN_DAYS("duration.inDays", 2) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.DURATION;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.DATE, Neo4JType.DATE };
            }
        },
        DURATION_IN_SECONDS("duration.inSeconds", 2) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.DURATION;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.DATE, Neo4JType.DATE };
            }
        },
        LEFT("left", 2) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.STRING;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.STRING, Neo4JType.INTEGER };
            }
        },
        RIGHT("right", 2) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.STRING;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.STRING, Neo4JType.INTEGER };
            }
        },
        LTRIM("lTrim", 1) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.STRING;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.STRING };
            }
        },
        RTRIM("rTrim", 1) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.STRING;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.STRING };
            }
        },
        TRIM("trim", 1) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.STRING;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.STRING };
            }
        },
        TO_LOWER("toLower", 1) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.STRING;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.STRING };
            }
        },
        TO_UPPER("toUpper", 1) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.STRING;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.STRING };
            }
        },
        REVERSE("reverse", 1) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.STRING;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.STRING };
            }
        },
        REPLACE("replace", 3) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.STRING;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.STRING, Neo4JType.STRING, Neo4JType.STRING };
            }
        },
        SUBSTRING("substring", 3) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.STRING;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.STRING, Neo4JType.INTEGER, Neo4JType.INTEGER };
            }
        },
        TO_STRING("toString", 1) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.STRING;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.getRandom() };
            }
        },
        TO_STRING_OR_NULL("toStringOrNull", 1) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.STRING;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.getRandom() };
            }
        },
        CEIL("ceil", 1) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.FLOAT;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Randomization.fromOptions(Neo4JType.INTEGER, Neo4JType.FLOAT) };
            }
        },
        FLOOR("floor", 1) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.FLOAT;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Randomization.fromOptions(Neo4JType.INTEGER, Neo4JType.FLOAT) };
            }
        },
        TO_FLOAT("toFloat", 1) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.FLOAT;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Randomization.fromOptions(Neo4JType.STRING,
                        Neo4JType.INTEGER,
                        Neo4JType.FLOAT) };
            }
        },
        TO_FLOAT_OR_NULL("toFloatOrNull", 1) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.FLOAT;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.getRandom() };
            }
        },
        POINT_DISTANCE("point.distance", 2) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.FLOAT;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.POINT, Neo4JType.POINT };
            }
        };

        private final String name;
        private final int arity;

        Neo4JFunction(String name, int arity) {
            this.name = name;
            this.arity = arity;
        }

        public int getArity() {
            return arity;
        }

        public String getName() {
            return name;
        }

        public abstract boolean supportReturnType(Neo4JType returnType);

        public abstract Neo4JType[] getArgumentTypes(Neo4JType returnType);

    }

}
