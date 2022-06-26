package ch.ethz.ast.gdblancer.redis.ast;

import ch.ethz.ast.gdblancer.cypher.ast.CypherFunctionDescription;
import ch.ethz.ast.gdblancer.common.schema.CypherType;
import ch.ethz.ast.gdblancer.util.Randomization;

// See: https://redis.io/commands/graph.query/#functions
public enum RedisFunction implements CypherFunctionDescription {

    ABS("abs", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.INTEGER || returnType == CypherType.FLOAT;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            assert returnType == CypherType.INTEGER || returnType == CypherType.FLOAT;
            return new CypherType[] { returnType };
        }
    },
    SIGN("sign", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.INTEGER;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[] { Randomization.fromOptions(CypherType.INTEGER, CypherType.FLOAT) };
        }
    },
    TO_INTEGER("toInteger", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.INTEGER;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            CypherType chosenType = Randomization.fromOptions(CypherType.STRING, CypherType.INTEGER, CypherType.FLOAT);
            return new CypherType[] { chosenType };
        }
    },
    LEFT("left", 2) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[] { CypherType.STRING, CypherType.INTEGER };
        }
    },
    RIGHT("right", 2) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[] { CypherType.STRING, CypherType.INTEGER };
        }
    },
    LTRIM("lTrim", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[] { CypherType.STRING };
        }
    },
    RTRIM("rTrim", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[] { CypherType.STRING };
        }
    },
    TRIM("trim", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[] { CypherType.STRING };
        }
    },
    TO_LOWER("toLower", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[] { CypherType.STRING };
        }
    },
    TO_UPPER("toUpper", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[] { CypherType.STRING };
        }
    },
    REVERSE("reverse", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[] { CypherType.STRING };
        }
    },
    REPLACE("replace", 3) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[] { CypherType.STRING, CypherType.STRING, CypherType.STRING };
        }
    },
    SUBSTRING("substring", 3) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[] { CypherType.STRING, CypherType.INTEGER, CypherType.INTEGER };
        }
    },
    TO_STRING("toString", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[] { Randomization.fromOptions(RedisExpressionGenerator.supportedTypes) };
        }
    },
    CEIL("ceil", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.FLOAT;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[] { Randomization.fromOptions(CypherType.INTEGER, CypherType.FLOAT) };
        }
    },
    FLOOR("floor", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.FLOAT;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[] { Randomization.fromOptions(CypherType.INTEGER, CypherType.FLOAT) };
        }
    },
    POINT_DISTANCE("distance", 2) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.FLOAT;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[] { CypherType.POINT, CypherType.POINT };
        }
    },
    ROUND("round", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.FLOAT;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            CypherType chosenType = Randomization.fromOptions(CypherType.INTEGER, CypherType.FLOAT);
            return new CypherType[] { chosenType };
        }
    },
    SQRT("sqrt", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.FLOAT;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            CypherType chosenType = Randomization.fromOptions(CypherType.INTEGER, CypherType.FLOAT);
            return new CypherType[] { chosenType };

        }
    },
    POW("pow", 2) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.FLOAT;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[] { CypherType.INTEGER, CypherType.INTEGER };
        }
    },
    SIZE("size", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.INTEGER;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[] { CypherType.STRING };
        }
    },
    TO_JSON("toJSON", 1) {
        @Override
        public boolean supportReturnType(CypherType returnType) {
            return returnType == CypherType.STRING;
        }

        @Override
        public CypherType[] getArgumentTypes(CypherType returnType) {
            return new CypherType[] { Randomization.fromOptions(RedisExpressionGenerator.supportedTypes) };
        }
    };

    private final String name;
    private final int arity;

    RedisFunction(String name, int arity) {
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