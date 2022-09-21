package ch.ethz.ast.gdbmeter.cypher.ast;

public class CypherVariablePropertyAccess implements CypherExpression {

    // TODO: Maybe split into Variable and property name (+type)
    private final String variableName;

    public CypherVariablePropertyAccess(String variableName) {
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }

}
