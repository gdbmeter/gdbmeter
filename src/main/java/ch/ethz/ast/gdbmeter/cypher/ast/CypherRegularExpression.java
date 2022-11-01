package ch.ethz.ast.gdbmeter.cypher.ast;

public record CypherRegularExpression(CypherExpression string,
                                      CypherExpression regex) implements CypherExpression {

}
