package ch.ethz.ast.gdblancer.redis.ast;

import ch.ethz.ast.gdblancer.cypher.ast.CypherFunctionDescription;
import ch.ethz.ast.gdblancer.redis.RedisBugs;
import ch.ethz.ast.gdblancer.redis.schema.RedisType;
import ch.ethz.ast.gdblancer.util.Randomization;

// See: https://redis.io/commands/graph.query/#functions
public enum RedisFunction implements CypherFunctionDescription<RedisType> {

    ABS("abs", 1) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.INTEGER || returnType == RedisType.FLOAT;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            assert returnType == RedisType.INTEGER || returnType == RedisType.FLOAT;
            return new RedisType[] { returnType };
        }
    },
    SIGN("sign", 1) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.INTEGER;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            return new RedisType[] { Randomization.fromOptions(RedisType.INTEGER, RedisType.FLOAT) };
        }
    },
    TO_INTEGER("toInteger", 1) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.INTEGER;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            RedisType chosenType = Randomization.fromOptions(RedisType.STRING, RedisType.INTEGER, RedisType.FLOAT);
            return new RedisType[] { chosenType };
        }
    },
    LEFT("left", 2) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.STRING;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            return new RedisType[] { RedisType.STRING, RedisType.INTEGER };
        }
    },
    RIGHT("right", 2) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.STRING;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            return new RedisType[] { RedisType.STRING, RedisType.INTEGER };
        }
    },
    LTRIM("lTrim", 1) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.STRING;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            return new RedisType[] { RedisType.STRING };
        }
    },
    RTRIM("rTrim", 1) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.STRING;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            return new RedisType[] { RedisType.STRING };
        }
    },
    TRIM("trim", 1) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.STRING;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            return new RedisType[] { RedisType.STRING };
        }
    },
    TO_LOWER("toLower", 1) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.STRING;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            return new RedisType[] { RedisType.STRING };
        }
    },
    TO_UPPER("toUpper", 1) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.STRING;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            return new RedisType[] { RedisType.STRING };
        }
    },
    REVERSE("reverse", 1) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.STRING;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            return new RedisType[] { RedisType.STRING };
        }
    },
    REPLACE("replace", 3) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.STRING;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            return new RedisType[] { RedisType.STRING, RedisType.STRING, RedisType.STRING };
        }
    },
    SUBSTRING("substring", 3) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.STRING;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            return new RedisType[] { RedisType.STRING, RedisType.INTEGER, RedisType.INTEGER };
        }
    },
    TO_STRING("toString", 1) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.STRING;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            return new RedisType[] { RedisType.getRandom() };
        }
    },
    CEIL("ceil", 1) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            if (RedisBugs.bug2440) {
                return returnType == RedisType.FLOAT || returnType == RedisType.INTEGER;
            } else {
                return returnType == RedisType.FLOAT;
            }
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            if (RedisBugs.bug2440) {
                if (returnType == RedisType.FLOAT) {
                    return new RedisType[] { RedisType.FLOAT };
                } else if (returnType == RedisType.INTEGER) {
                    return new RedisType[] { RedisType.INTEGER };
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                return new RedisType[] { Randomization.fromOptions(RedisType.INTEGER, RedisType.FLOAT) };
            }
        }
    },
    FLOOR("floor", 1) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            if (RedisBugs.bug2440) {
                return returnType == RedisType.FLOAT || returnType == RedisType.INTEGER;
            } else {
                return returnType == RedisType.FLOAT;
            }
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            if (RedisBugs.bug2440) {
                if (returnType == RedisType.FLOAT) {
                    return new RedisType[] { RedisType.FLOAT };
                } else if (returnType == RedisType.INTEGER) {
                    return new RedisType[] { RedisType.INTEGER };
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                return new RedisType[] { Randomization.fromOptions(RedisType.INTEGER, RedisType.FLOAT) };
            }
        }
    },
    POINT_DISTANCE("distance", 2) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.FLOAT;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            return new RedisType[] { RedisType.POINT, RedisType.POINT };
        }
    },
    ROUND("round", 1) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.FLOAT;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            RedisType chosenType = Randomization.fromOptions(RedisType.INTEGER, RedisType.FLOAT);
            return new RedisType[] { chosenType };
        }
    },
    SQRT("sqrt", 1) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.FLOAT;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            RedisType chosenType = Randomization.fromOptions(RedisType.INTEGER, RedisType.FLOAT);
            return new RedisType[] { chosenType };

        }
    },
    POW("pow", 2) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.FLOAT;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            return new RedisType[] { RedisType.INTEGER, RedisType.INTEGER };
        }
    },
    SIZE("size", 1) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.INTEGER;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            return new RedisType[] { RedisType.STRING };
        }
    },
    TO_JSON("toJSON", 1) {
        @Override
        public boolean supportReturnType(RedisType returnType) {
            return returnType == RedisType.STRING;
        }

        @Override
        public RedisType[] getArgumentTypes(RedisType returnType) {
            return new RedisType[] { RedisType.getRandom() };
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