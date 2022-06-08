package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.cypher.schema.CypherType;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.neo4j.ast.Neo4JExpressionGenerator;
import ch.ethz.ast.gdblancer.neo4j.gen.util.Neo4JDBUtil;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Map;

public class Neo4JDeleteGenerator {

    private final CypherSchema schema;
    private final StringBuilder query = new StringBuilder();
    private final ExpectedErrors errors = new ExpectedErrors();

    public Neo4JDeleteGenerator(CypherSchema schema) {
        this.schema = schema;
    }

    public static Neo4JQuery deleteNodes(CypherSchema schema) {
        return new Neo4JDeleteGenerator(schema).generateDelete();
    }

    // TODO: Support DELETE of relationships
    // TODO: Support DELETE of nodes of different labels
    // TODO: Add RETURN clause
    private Neo4JQuery generateDelete() {
        Neo4JDBUtil.addRegexErrors(errors);
        Neo4JDBUtil.addArithmeticErrors(errors);
        Neo4JDBUtil.addFunctionErrors(errors);

        String label = schema.getRandomLabel();
        CypherEntity entity = schema.getEntityByLabel(label);

        query.append(String.format("MATCH (n:%s)", label));
        query.append(" WHERE ");
        query.append(CypherVisitor.asString(Neo4JExpressionGenerator.generateExpression(Map.of("n", entity), CypherType.BOOLEAN)));

        if (Randomization.getBoolean()) {
            query.append(" DETACH");
        } else {
            errors.addRegex("Cannot delete node<\\d+>, because it still has relationships. To delete this node, you must first delete its relationships.");
        }

        query.append(" DELETE n");
        return new Neo4JQuery(query.toString(), errors);
    }

}
