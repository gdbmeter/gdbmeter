package ch.ethz.ast.gdbmeter.cypher.ast;

public interface CypherFunctionDescription<T> {

    int getArity();
    String getName();
    boolean supportReturnType(T returnType);
    T[] getArgumentTypes(T returnType);

}
