package ch.ethz.ast.gdbmeter.neo4j.ast;

import ch.ethz.ast.gdbmeter.cypher.ast.CypherFunctionDescription;
import ch.ethz.ast.gdbmeter.neo4j.schema.Neo4JType;
import ch.ethz.ast.gdbmeter.util.Randomization;

public enum Neo4JFunction implements CypherFunctionDescription<Neo4JType> {

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

            return new Neo4JType[]{chosenType};
        }
    },
    TO_BOOLEAN_OR_NULL("toBooleanOrNull", 1) {
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
            return new Neo4JType[]{returnType};
        }
    },
    SIGN("sign", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.INTEGER;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Randomization.fromOptions(Neo4JType.INTEGER, Neo4JType.FLOAT)};
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

            return new Neo4JType[]{chosenType};
        }
    },
    TO_INTEGER_OR_NULL("toIntegerOrNull", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.INTEGER;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.getRandom()};
        }
    },
    DURATION_BETWEEN("duration.between", 2) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.DURATION;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.DATE, Neo4JType.DATE};
        }
    },
    DURATION_IN_MONTHS("duration.inMonths", 2) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.DURATION;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.DATE, Neo4JType.DATE};
        }
    },
    DURATION_IN_DAYS("duration.inDays", 2) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.DURATION;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.DATE, Neo4JType.DATE};
        }
    },
    DURATION_IN_SECONDS("duration.inSeconds", 2) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.DURATION;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.DATE, Neo4JType.DATE};
        }
    },
    LEFT("left", 2) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.STRING;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.STRING, Neo4JType.INTEGER};
        }
    },
    RIGHT("right", 2) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.STRING;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.STRING, Neo4JType.INTEGER};
        }
    },
    LTRIM("lTrim", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.STRING;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.STRING};
        }
    },
    RTRIM("rTrim", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.STRING;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.STRING};
        }
    },
    TRIM("trim", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.STRING;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.STRING};
        }
    },
    TO_LOWER("toLower", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.STRING;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.STRING};
        }
    },
    TO_UPPER("toUpper", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.STRING;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.STRING};
        }
    },
    REVERSE("reverse", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.STRING;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.STRING};
        }
    },
    REPLACE("replace", 3) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.STRING;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.STRING, Neo4JType.STRING, Neo4JType.STRING};
        }
    },
    SUBSTRING("substring", 3) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.STRING;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.STRING, Neo4JType.INTEGER, Neo4JType.INTEGER};
        }
    },
    TO_STRING("toString", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.STRING;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.getRandom()};
        }
    },
    TO_STRING_OR_NULL("toStringOrNull", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.STRING;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.getRandom()};
        }
    },
    CEIL("ceil", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.FLOAT;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Randomization.fromOptions(Neo4JType.INTEGER, Neo4JType.FLOAT)};
        }
    },
    FLOOR("floor", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.FLOAT;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Randomization.fromOptions(Neo4JType.INTEGER, Neo4JType.FLOAT)};
        }
    },
    TO_FLOAT("toFloat", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.FLOAT;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Randomization.fromOptions(Neo4JType.STRING,
                    Neo4JType.INTEGER,
                    Neo4JType.FLOAT)};
        }
    },
    TO_FLOAT_OR_NULL("toFloatOrNull", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.FLOAT;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.getRandom()};
        }
    },
    POINT_DISTANCE("point.distance", 2) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.FLOAT;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.POINT, Neo4JType.POINT};
        }
    },
    IS_EMPTY("isEmpty", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.BOOLEAN;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.STRING};
        }
    },
    SIZE("size", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.INTEGER;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.STRING};
        }
    },
    ROUND("round", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.FLOAT;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{Neo4JType.FLOAT};
        }
    },
    E("e", 0) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.FLOAT;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{};
        }
    },
    EXP("exp", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.FLOAT;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{ Neo4JType.FLOAT };
        }
    },
    LOG("log", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.FLOAT;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{ Neo4JType.FLOAT };
        }
    },
    LOG_10("log10", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.FLOAT;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{ Neo4JType.FLOAT };
        }
    },
    SQRT("sqrt", 1) {
        @Override
        public boolean supportReturnType(Neo4JType returnType) {
            return returnType == Neo4JType.FLOAT;
        }

        @Override
        public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
            return new Neo4JType[]{ Neo4JType.FLOAT };
        }
    };

    private final String name;
    private final int arity;

    Neo4JFunction(String name, int arity) {
        this.name = name;
        this.arity = arity;
    }

    @Override
    public int getArity() {
        return arity;
    }

    @Override
    public String getName() {
        return name;
    }

}
