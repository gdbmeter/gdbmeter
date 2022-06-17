package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.cypher.ast.CypherExpression;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.cypher.schema.CypherType;
import ch.ethz.ast.gdblancer.redis.RedisQuery;
import ch.ethz.ast.gdblancer.redis.RedisUtil;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Map;
import java.util.Set;

public class RedisSetGenerator {

    private final CypherSchema schema;
    private final StringBuilder query = new StringBuilder();
    private final ExpectedErrors errors = new ExpectedErrors();

    public RedisSetGenerator(CypherSchema schema) {
        this.schema = schema;
    }

    public static RedisQuery setProperties(CypherSchema schema) {
        return new RedisSetGenerator(schema).generateSet();
    }

    // TODO: Support SET on relationships
    // TODO: Support SET on nodes with different labels
    // TODO: Support SET of multiple properties
    // TODO: Add RETURN clause
    private RedisQuery generateSet() {
        String label = schema.getRandomLabel();
        CypherEntity entity = schema.getEntityByLabel(label);

        RedisUtil.addFunctionErrors(errors);
        RedisUtil.addArithmeticErrors(errors);

        query.append(String.format("MATCH (n:%s)", label));
        query.append(" WHERE ");
        query.append(CypherVisitor.asString(RedisExpressionGenerator.generateExpression(Map.of("n", entity), CypherType.BOOLEAN)));

        if (Randomization.smallBiasProbability()) {
            query.append(" SET n = {}");
        } else {
            Set<String> properties = entity.getAvailableProperties().keySet();
            String property = Randomization.fromSet(properties);
            CypherType type = entity.getAvailableProperties().get(property);
            CypherExpression expression;

            if (Randomization.getBoolean()) {
                expression = RedisExpressionGenerator.generateConstant(type);
            } else {
                expression = RedisExpressionGenerator.generateExpression(type);
            }

            query.append(String.format(" SET n.%s = %s", property, CypherVisitor.asString(expression)));
        }

        return new RedisQuery(query.toString(), errors);
    }


}
