package ch.ethz.ast.gdblancer.cypher.ast;

import ch.ethz.ast.gdblancer.cypher.schema.CypherType;

public interface CypherFunctionDescription {

    int getArity();
    String getName();
    boolean supportReturnType(CypherType returnType);
    CypherType[] getArgumentTypes(CypherType returnType);

}
