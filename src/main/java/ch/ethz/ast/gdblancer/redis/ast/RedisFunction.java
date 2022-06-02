package ch.ethz.ast.gdblancer.redis.ast;

import ch.ethz.ast.gdblancer.cypher.ast.CypherFunctionDescription;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JType;
import ch.ethz.ast.gdblancer.util.Randomization;

// See: https://redis.io/commands/graph.query/#functions
public enum RedisFunction implements CypherFunctionDescription {

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
            Neo4JType chosenType = Randomization.fromOptions(Neo4JType.STRING, Neo4JType.INTEGER, Neo4JType.FLOAT);

            return new Neo4JType[] { chosenType };
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
            return new Neo4JType[] { Randomization.fromOptions(RedisExpressionGenerator.supportedTypes) };
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
    POINT_DISTANCE("distance", 2) {
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