package ch.ethz.ast.gdbmeter.cypher.ast;

public record CypherFunctionCall<T>(CypherFunctionDescription<T> function,
                                    CypherExpression[] arguments) implements CypherExpression {

    public String functionName() {
        return function.getName();
    }

}
