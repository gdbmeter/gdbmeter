package ch.ethz.ast.gdblancer.neo4j.gen.ast;

public class Neo4JRegularExpression implements Neo4JExpression {

    private final Neo4JExpression string;
    private final Neo4JExpression regex;

    public Neo4JRegularExpression(Neo4JExpression string, Neo4JExpression regex) {
        this.string = string;
        this.regex = regex;
    }

    public Neo4JExpression getString() {
        return string;
    }

    public Neo4JExpression getRegex() {
        return regex;
    }

}
