package ch.ethz.ast.gdbmeter.neo4j.gen;

import ch.ethz.ast.gdbmeter.common.ExpectedErrors;
import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdbmeter.cypher.gen.CypherRemoveGenerator;
import ch.ethz.ast.gdbmeter.neo4j.Neo4JQuery;
import ch.ethz.ast.gdbmeter.neo4j.Neo4JUtil;
import ch.ethz.ast.gdbmeter.neo4j.ast.Neo4JExpressionGenerator;
import ch.ethz.ast.gdbmeter.neo4j.schema.Neo4JType;

import java.util.Map;

public class Neo4JRemoveGenerator extends CypherRemoveGenerator<Neo4JType> {

    public Neo4JRemoveGenerator(Schema<Neo4JType> schema) {
        super(schema);
    }

    public static Neo4JQuery removeProperties(Schema<Neo4JType> schema) {
        Neo4JRemoveGenerator generator = new Neo4JRemoveGenerator(schema);
        generator.generateRemove();

        ExpectedErrors errors = new ExpectedErrors();
        Neo4JUtil.addRegexErrors(errors);
        Neo4JUtil.addArithmeticErrors(errors);
        Neo4JUtil.addFunctionErrors(errors);

        return new Neo4JQuery(generator.query.toString(), errors);
    }

    @Override
    protected String generateWhereClause(Entity<Neo4JType> entity) {
        return CypherVisitor.asString(Neo4JExpressionGenerator.generateExpression(Map.of("n", entity), Neo4JType.BOOLEAN));
    }

    @Override
    protected String generateRemoveClause(String property) {
        return String.format(" REMOVE n.%s", property);
    }

}
