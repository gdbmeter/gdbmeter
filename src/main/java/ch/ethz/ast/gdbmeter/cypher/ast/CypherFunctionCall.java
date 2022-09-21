package ch.ethz.ast.gdbmeter.cypher.ast;

public class CypherFunctionCall<T> implements CypherExpression {

    private final CypherFunctionDescription<T> function;
    private final CypherExpression[] arguments;

    public CypherFunctionCall(CypherFunctionDescription<T> function,
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
