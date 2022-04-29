package ch.ethz.ast.gdblancer.neo4j.gen.ast;

public class Neo4JVariablePropertyAccess implements Neo4JExpression {

    // TODO: Maybe split into Variable and property name (+type)
    private final String variableName;

    public Neo4JVariablePropertyAccess(String variableName) {
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }

}
