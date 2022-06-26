package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.cypher.ast.CypherExpression;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.cypher.gen.CypherSetGenerator;
import ch.ethz.ast.gdblancer.common.schema.CypherEntity;
import ch.ethz.ast.gdblancer.common.schema.CypherSchema;
import ch.ethz.ast.gdblancer.common.schema.CypherType;
import ch.ethz.ast.gdblancer.redis.RedisQuery;
import ch.ethz.ast.gdblancer.redis.RedisUtil;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;

import java.util.Map;

public class RedisSetGenerator extends CypherSetGenerator {

    public RedisSetGenerator(CypherSchema schema) {
        super(schema);
    }

    public static RedisQuery setProperties(CypherSchema schema) {
        ExpectedErrors errors = new ExpectedErrors();

        RedisUtil.addFunctionErrors(errors);
        RedisUtil.addArithmeticErrors(errors);


        RedisSetGenerator generator = new RedisSetGenerator(schema);
        generator.generateSet();
        return new RedisQuery(generator.query.toString(), errors);
    }

    @Override
    protected String generateWhereClause(CypherEntity entity) {
        return CypherVisitor.asString(RedisExpressionGenerator.generateExpression(Map.of("n", entity), CypherType.BOOLEAN));
    }

    @Override
    protected CypherExpression generateConstant(CypherType type) {
        return RedisExpressionGenerator.generateConstant(type);
    }

    @Override
    protected CypherExpression generateExpression(CypherType type) {
        return RedisExpressionGenerator.generateExpression(type);
    }

}
