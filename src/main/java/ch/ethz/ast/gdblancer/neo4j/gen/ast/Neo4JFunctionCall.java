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

        TO_BOOLEAN("toBoolean", 1, Neo4JType.BOOLEAN) {
            @Override
            public Neo4JType[] getArgumentTypes() {
                Neo4JType chosenType = Randomization.fromOptions(Neo4JType.BOOLEAN,
                        Neo4JType.STRING,
                        Neo4JType.INTEGER);

                return new Neo4JType[] { chosenType };
            }
        },
        TO_BOOLEAN_OR_NULL("toBooleanOrNull" , 1, Neo4JType.BOOLEAN) {
            @Override
            public Neo4JType[] getArgumentTypes() {
                return new Neo4JType[]{Neo4JType.getRandom()};
            }
        };

        private final String name;
        private final int arity;
        private final Neo4JType returnType;

        Neo4JFunction(String name,
                      int arity,
                      Neo4JType returnType) {
            this.name = name;
            this.arity = arity;
            this.returnType = returnType;
        }

        public Neo4JType getReturnType() {
            return returnType;
        }

        public int getArity() {
            return arity;
        }

        public String getName() {
            return name;
        }

        public abstract Neo4JType[] getArgumentTypes();

    }


}
