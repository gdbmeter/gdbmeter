package ch.ethz.ast.gdblancer.neo4j.gen.ast;

import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JType;
import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JFunctionCall implements Neo4JExpression {

    private final Neo4JFunction function;
    private final Neo4JExpression[] arguments;

    Neo4JFunctionCall(Neo4JFunction function,
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
                return returnType == Neo4JType.INTEGER;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.INTEGER };
            }
        },
        SIGN("sign", 1) {
            @Override
            public boolean supportReturnType(Neo4JType returnType) {
                return returnType == Neo4JType.INTEGER;
            }

            @Override
            public Neo4JType[] getArgumentTypes(Neo4JType returnType) {
                return new Neo4JType[] { Neo4JType.INTEGER };
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
