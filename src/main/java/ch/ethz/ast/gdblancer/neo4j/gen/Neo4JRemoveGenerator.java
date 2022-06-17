package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.cypher.schema.CypherType;
import ch.ethz.ast.gdblancer.neo4j.ast.Neo4JExpressionGenerator;
import ch.ethz.ast.gdblancer.neo4j.Neo4JUtil;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Map;

public class Neo4JRemoveGenerator {

    private final CypherSchema schema;
    private final StringBuilder query = new StringBuilder();
    private final ExpectedErrors errors = new ExpectedErrors();

    public Neo4JRemoveGenerator(CypherSchema schema) {
        this.schema = schema;
    }

    public static Neo4JQuery removeProperties(CypherSchema schema) {
        return new Neo4JRemoveGenerator(schema).generateRemove();
    }

    // TODO: Support REMOVE on nodes with different labels
    // TODO: Support REMOVE of multiple properties
    // TODO: Add RETURN clause
    private Neo4JQuery generateRemove() {
        Neo4JUtil.addRegexErrors(errors);
        Neo4JUtil.addArithmeticErrors(errors);
        Neo4JUtil.addFunctionErrors(errors);

        String label = schema.getRandomLabel();
        CypherEntity entity = schema.getEntityByLabel(label);

        query.append(String.format("MATCH (n:%s)", label));
        query.append(" WHERE ");
        query.append(CypherVisitor.asString(Neo4JExpressionGenerator.generateExpression(Map.of("n", entity), CypherType.BOOLEAN)));

        String property = Randomization.fromSet(entity.getAvailableProperties().keySet());
        query.append(String.format(" REMOVE n.%s ", property));

        return new Neo4JQuery(query.toString(), errors);
    }


}
