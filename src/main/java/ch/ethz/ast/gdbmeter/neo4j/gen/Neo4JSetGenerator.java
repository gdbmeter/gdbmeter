package ch.ethz.ast.gdbmeter.neo4j.gen;

import ch.ethz.ast.gdbmeter.common.ExpectedErrors;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.cypher.ast.CypherExpression;
import ch.ethz.ast.gdbmeter.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdbmeter.cypher.gen.CypherSetGenerator;
import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.neo4j.Neo4JQuery;
import ch.ethz.ast.gdbmeter.neo4j.Neo4JUtil;
import ch.ethz.ast.gdbmeter.neo4j.ast.Neo4JExpressionGenerator;
import ch.ethz.ast.gdbmeter.neo4j.schema.Neo4JType;

import java.util.Map;

public class Neo4JSetGenerator extends CypherSetGenerator<Neo4JType> {

    public Neo4JSetGenerator(Schema<Neo4JType> schema) {
        super(schema);
    }

    public static Neo4JQuery setProperties(Schema<Neo4JType> schema) {
        ExpectedErrors errors = new ExpectedErrors();

        Neo4JUtil.addRegexErrors(errors);
        Neo4JUtil.addArithmeticErrors(errors);
        Neo4JUtil.addFunctionErrors(errors);


        Neo4JSetGenerator generator = new Neo4JSetGenerator(schema);
        generator.generateSet();
        return new Neo4JQuery(generator.query.toString(), errors);
    }

    @Override
    protected String generateWhereClause(Entity<Neo4JType> entity) {
        return CypherVisitor.asString(Neo4JExpressionGenerator.generateExpression(Map.of("n", entity), Neo4JType.BOOLEAN));
    }

    @Override
    protected CypherExpression generateConstant(Neo4JType type) {
        return Neo4JExpressionGenerator.generateConstant(type);
    }

    // TODO: Use n as a variable here
    @Override
    protected CypherExpression generateExpression(Neo4JType type) {
        return Neo4JExpressionGenerator.generateExpression(type);
    }


}
