package ch.ethz.ast.gdblancer.neo4j.ast;

import ch.ethz.ast.gdblancer.cypher.ast.CypherFunctionDescription;
import ch.ethz.ast.gdblancer.cypher.schema.CypherType;
import ch.ethz.ast.gdblancer.util.Randomization;

public enum Neo4JFunction implements CypherFunctionDescription {

    TO_BOOLEAN("toBoolean", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.BOOLEAN;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            CypherType chosenType = Randomization.fromOptions(CypherType.BOOLEAN,
                    CypherType.STRING,
                    CypherType.INTEGER);

            return new CypherType[]{chosenType};
        }
    },
    TO_BOOLEAN_OR_NULL("toBooleanOrNull", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.BOOLEAN;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.getRandom()};
        }
    },
    ABS("abs", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.INTEGER || returnType == CypherType.FLOAT;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            assert returnType == CypherType.INTEGER || returnType == CypherType.FLOAT;
            return new CypherType[]{returnType};
        }
    },
    SIGN("sign", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.INTEGER;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{Randomization.fromOptions(CypherType.INTEGER, CypherType.FLOAT)};
        }
    },
    TO_INTEGER("toInteger", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.INTEGER;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            CypherType chosenType = Randomization.fromOptions(CypherType.BOOLEAN,
                    CypherType.STRING,
                    CypherType.INTEGER,
                    CypherType.FLOAT);

            return new CypherType[]{chosenType};
        }
    },
    TO_INTEGER_OR_NULL("toIntegerOrNull", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.INTEGER;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.getRandom()};
        }
    },
    DURATION_BETWEEN("duration.between", 2) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.DURATION;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.DATE, CypherType.DATE};
        }
    },
    DURATION_IN_MONTHS("duration.inMonths", 2) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.DURATION;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.DATE, CypherType.DATE};
        }
    },
    DURATION_IN_DAYS("duration.inDays", 2) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.DURATION;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.DATE, CypherType.DATE};
        }
    },
    DURATION_IN_SECONDS("duration.inSeconds", 2) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.DURATION;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.DATE, CypherType.DATE};
        }
    },
    LEFT("left", 2) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.STRING, CypherType.INTEGER};
        }
    },
    RIGHT("right", 2) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.STRING, CypherType.INTEGER};
        }
    },
    LTRIM("lTrim", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.STRING};
        }
    },
    RTRIM("rTrim", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.STRING};
        }
    },
    TRIM("trim", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.STRING};
        }
    },
    TO_LOWER("toLower", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.STRING};
        }
    },
    TO_UPPER("toUpper", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.STRING};
        }
    },
    REVERSE("reverse", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.STRING};
        }
    },
    REPLACE("replace", 3) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.STRING, CypherType.STRING, CypherType.STRING};
        }
    },
    SUBSTRING("substring", 3) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.STRING, CypherType.INTEGER, CypherType.INTEGER};
        }
    },
    TO_STRING("toString", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.getRandom()};
        }
    },
    TO_STRING_OR_NULL("toStringOrNull", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.getRandom()};
        }
    },
    CEIL("ceil", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.FLOAT;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{Randomization.fromOptions(CypherType.INTEGER, CypherType.FLOAT)};
        }
    },
    FLOOR("floor", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.FLOAT;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{Randomization.fromOptions(CypherType.INTEGER, CypherType.FLOAT)};
        }
    },
    TO_FLOAT("toFloat", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.FLOAT;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{Randomization.fromOptions(CypherType.STRING,
                    CypherType.INTEGER,
                    CypherType.FLOAT)};
        }
    },
    TO_FLOAT_OR_NULL("toFloatOrNull", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.FLOAT;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.getRandom()};
        }
    },
    POINT_DISTANCE("point.distance", 2) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.FLOAT;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.POINT, CypherType.POINT};
        }
    },
    IS_EMPTY("isEmpty", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.BOOLEAN;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.STRING};
        }
    },
    SIZE("size", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.INTEGER;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.STRING};
        }
    },
    ROUND("round", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.FLOAT;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{CypherType.FLOAT};
        }
    },
    E("e", 0) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.FLOAT;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{};
        }
    },
    EXP("exp", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.FLOAT;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{ CypherType.FLOAT };
        }
    },
    LOG("log", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.FLOAT;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{ CypherType.FLOAT };
        }
    },
    LOG_10("log10", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.FLOAT;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{ CypherType.FLOAT };
        }
    },
    SQRT("sqrt", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.FLOAT;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[]{ CypherType.FLOAT };
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
