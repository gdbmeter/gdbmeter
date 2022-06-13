package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.cypher.ast.CypherExpression;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.cypher.schema.CypherType;
import ch.ethz.ast.gdblancer.neo4j.ast.Neo4JExpressionGenerator;
import ch.ethz.ast.gdblancer.neo4j.Neo4JUtil;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Map;
import java.util.Set;

public class Neo4JSetGenerator {

    private final CypherSchema schema;
    private final StringBuilder query = new StringBuilder();
    private final ExpectedErrors errors = new ExpectedErrors();

    public Neo4JSetGenerator(CypherSchema schema) {
        this.schema = schema;
    }

    public static Neo4JQuery setProperties(CypherSchema schema) {
        return new Neo4JSetGenerator(schema).generateSet();
    }

    // TODO: Support SET on relationships
    // TODO: Support SET on nodes with different labels
    // TODO: Support SET of multiple properties
    // TODO: Add RETURN clause
    private Neo4JQuery generateSet() {
        Neo4JUtil.addRegexErrors(errors);
        Neo4JUtil.addArithmeticErrors(errors);
        Neo4JUtil.addFunctionErrors(errors);

        String label = schema.getRandomLabel();
        CypherEntity entity = schema.getEntityByLabel(label);

        query.append(String.format("MATCH (n:%s)", label));
        query.append(" WHERE ");
        query.append(CypherVisitor.asString(Neo4JExpressionGenerator.generateExpression(Map.of("n", entity), CypherType.BOOLEAN)));

        if (Randomization.smallBiasProbability()) {
            query.append(" SET n = {}");
        } else {
            Set<String> properties = entity.getAvailableProperties().keySet();
            String property = Randomization.fromSet(properties);
            CypherType type = entity.getAvailableProperties().get(property);
            CypherExpression expression;

            if (Randomization.getBoolean()) {
                expression = Neo4JExpressionGenerator.generateConstant(type);
            } else {
                expression = Neo4JExpressionGenerator.generateExpression(type);
            }

            query.append(String.format(" SET n.%s = %s", property, CypherVisitor.asString(expression)));

        }

        return new Neo4JQuery(query.toString(), errors);
    }


}
