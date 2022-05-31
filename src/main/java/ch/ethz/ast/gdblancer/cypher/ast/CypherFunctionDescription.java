package ch.ethz.ast.gdblancer.cypher.ast;

import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JType;

public interface CypherFunctionDescription {

    int getArity();
    String getName();
    boolean supportReturnType(Neo4JType returnType);
    Neo4JType[] getArgumentTypes(Neo4JType returnType);

}
