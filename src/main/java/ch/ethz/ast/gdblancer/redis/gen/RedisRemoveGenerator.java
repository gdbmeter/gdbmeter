package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.cypher.gen.CypherRemoveGenerator;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.cypher.schema.CypherType;
import ch.ethz.ast.gdblancer.redis.RedisQuery;
import ch.ethz.ast.gdblancer.redis.RedisUtil;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;

import java.util.Map;

public class RedisRemoveGenerator extends CypherRemoveGenerator  {

    public RedisRemoveGenerator(CypherSchema schema) {
        super(schema);
    }

    public static RedisQuery removeProperties(CypherSchema schema) {
        RedisRemoveGenerator generator = new RedisRemoveGenerator(schema);
        generator.generateRemove();

        ExpectedErrors errors = new ExpectedErrors();
        RedisUtil.addFunctionErrors(errors);
        RedisUtil.addArithmeticErrors(errors);

        return new RedisQuery(generator.query.toString(), errors);
    }

    @Override
    protected String generateWhereClause(CypherEntity entity) {
        return CypherVisitor.asString(RedisExpressionGenerator.generateExpression(Map.of("n", entity), CypherType.BOOLEAN));
    }

    /**
     * RedisGraph does not support REMOVE on a property.
     * Instead, we just set the property to NULL.
     */
    @Override
    protected String generateRemoveClause(String property) {
        return String.format(" SET n.%s = NULL", property);
    }


}
