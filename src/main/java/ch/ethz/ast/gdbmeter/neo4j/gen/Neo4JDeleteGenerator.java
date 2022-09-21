package ch.ethz.ast.gdbmeter.neo4j.gen;

import ch.ethz.ast.gdbmeter.common.ExpectedErrors;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdbmeter.cypher.gen.CypherDeleteGenerator;
import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.neo4j.Neo4JQuery;
import ch.ethz.ast.gdbmeter.neo4j.Neo4JUtil;
import ch.ethz.ast.gdbmeter.neo4j.ast.Neo4JExpressionGenerator;
import ch.ethz.ast.gdbmeter.neo4j.schema.Neo4JType;

import java.util.Map;

public class Neo4JDeleteGenerator extends CypherDeleteGenerator<Neo4JType> {

    private final ExpectedErrors errors = new ExpectedErrors();

    public Neo4JDeleteGenerator(Schema<Neo4JType> schema) {
        super(schema);
    }

    public static Neo4JQuery deleteNodes(Schema<Neo4JType> schema) {
        Neo4JDeleteGenerator generator = new Neo4JDeleteGenerator(schema);
        generator.generateDelete();

        Neo4JUtil.addRegexErrors(generator.errors);
        Neo4JUtil.addArithmeticErrors(generator.errors);
        Neo4JUtil.addFunctionErrors(generator.errors);

        return new Neo4JQuery(generator.query.toString(), generator.errors);
    }

    @Override
    protected String generateWhereClause(Entity<Neo4JType> entity) {
        return CypherVisitor.asString(Neo4JExpressionGenerator.generateExpression(Map.of("n", entity), Neo4JType.BOOLEAN));
    }

    @Override
    protected void onNonDetachDelete() {
        errors.addRegex("Cannot delete node<\\d+>, because it still has relationships. To delete this node, you must first delete its relationships.");
    }

}
