package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.cypher.gen.CypherDeleteGenerator;
import ch.ethz.ast.gdblancer.common.schema.CypherEntity;
import ch.ethz.ast.gdblancer.common.schema.CypherSchema;
import ch.ethz.ast.gdblancer.common.schema.CypherType;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.neo4j.Neo4JUtil;
import ch.ethz.ast.gdblancer.neo4j.ast.Neo4JExpressionGenerator;

import java.util.Map;

public class Neo4JDeleteGenerator extends CypherDeleteGenerator {

    private final ExpectedErrors errors = new ExpectedErrors();

    public Neo4JDeleteGenerator(CypherSchema schema) {
        super(schema);
    }

    public static Neo4JQuery deleteNodes(CypherSchema schema) {
        Neo4JDeleteGenerator generator = new Neo4JDeleteGenerator(schema);
        generator.generateDelete();

        Neo4JUtil.addRegexErrors(generator.errors);
        Neo4JUtil.addArithmeticErrors(generator.errors);
        Neo4JUtil.addFunctionErrors(generator.errors);

        return new Neo4JQuery(generator.query.toString(), generator.errors);
    }

    @Override
    protected String generateWhereClause(CypherEntity entity) {
        return CypherVisitor.asString(Neo4JExpressionGenerator.generateExpression(Map.of("n", entity), CypherType.BOOLEAN));
    }

    @Override
    protected void onNonDetachDelete() {
        errors.addRegex("Cannot delete node<\\d+>, because it still has relationships. To delete this node, you must first delete its relationships.");
    }

}
