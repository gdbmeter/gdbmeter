package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.cypher.schema.CypherType;
import ch.ethz.ast.gdblancer.redis.RedisQuery;
import ch.ethz.ast.gdblancer.redis.RedisUtil;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Map;

public class RedisDeleteGenerator {

    private final CypherSchema schema;
    private final StringBuilder query = new StringBuilder();
    private final ExpectedErrors errors = new ExpectedErrors();

    public RedisDeleteGenerator(CypherSchema schema) {
        this.schema = schema;
    }

    public static RedisQuery deleteNodes(CypherSchema schema) {
        return new RedisDeleteGenerator(schema).generateDelete();
    }

    // TODO: Support DELETE of relationships
    // TODO: Support DELETE of nodes of different labels
    // TODO: Add RETURN clause
    private RedisQuery generateDelete() {
        String label = schema.getRandomLabel();
        CypherEntity entity = schema.getEntityByLabel(label);

        RedisUtil.addFunctionErrors(errors);

        query.append(String.format("MATCH (n:%s)", label));
        query.append(" WHERE ");
        query.append(CypherVisitor.asString(RedisExpressionGenerator.generateExpression(Map.of("n", entity), CypherType.BOOLEAN)));

        if (Randomization.getBoolean()) {
            query.append(" DETACH");
        }

        query.append(" DELETE n");
        return new RedisQuery(query.toString(), errors);
    }

}