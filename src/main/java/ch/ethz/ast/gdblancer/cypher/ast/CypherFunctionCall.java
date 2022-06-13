package ch.ethz.ast.gdblancer.cypher.ast;

import ch.ethz.ast.gdblancer.cypher.schema.CypherType;
import ch.ethz.ast.gdblancer.util.Randomization;

public class CypherFunctionCall implements CypherExpression {

    private final CypherFunctionDescription function;
    private final CypherExpression[] arguments;

    public CypherFunctionCall(CypherFunctionDescription function,
                              CypherExpression[] arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    public String getFunctionName() {
        return function.getName();
    }

    public CypherExpression[] getArguments() {
        return arguments;
    }

}
