package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.cypher.gen.CypherRemoveGenerator;
import ch.ethz.ast.gdblancer.common.schema.CypherEntity;
import ch.ethz.ast.gdblancer.common.schema.CypherSchema;
import ch.ethz.ast.gdblancer.common.schema.CypherType;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.neo4j.Neo4JUtil;
import ch.ethz.ast.gdblancer.neo4j.ast.Neo4JExpressionGenerator;

import java.util.Map;

public class Neo4JRemoveGenerator extends CypherRemoveGenerator {

    public Neo4JRemoveGenerator(CypherSchema schema) {
        super(schema);
    }

    public static Neo4JQuery removeProperties(CypherSchema schema) {
        Neo4JRemoveGenerator generator = new Neo4JRemoveGenerator(schema);
        generator.generateRemove();

        ExpectedErrors errors = new ExpectedErrors();
        Neo4JUtil.addRegexErrors(errors);
        Neo4JUtil.addArithmeticErrors(errors);
        Neo4JUtil.addFunctionErrors(errors);

        return new Neo4JQuery(generator.query.toString(), errors);
    }

    @Override
    protected String generateWhereClause(CypherEntity entity) {
        return CypherVisitor.asString(Neo4JExpressionGenerator.generateExpression(Map.of("n", entity), CypherType.BOOLEAN));
    }

    @Override
    protected String generateRemoveClause(String property) {
        return String.format(" REMOVE n.%s", property);
    }

}
