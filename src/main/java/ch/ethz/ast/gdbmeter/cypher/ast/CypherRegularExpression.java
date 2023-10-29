package ch.ethz.ast.gdbmeter.cypher.ast;

public class CypherRegularExpression implements CypherExpression {

    private final CypherExpression string;
    private final CypherExpression regex;

    public CypherRegularExpression(CypherExpression string, CypherExpression regex) {
        this.string = string;
        this.regex = regex;
    }

    public CypherExpression getString() {
        return string;
    }

    public CypherExpression getRegex() {
        return regex;
    }

}
