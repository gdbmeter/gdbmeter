package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.cypher.gen.CypherDeleteGenerator;
import ch.ethz.ast.gdblancer.common.schema.Entity;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.redis.RedisQuery;
import ch.ethz.ast.gdblancer.redis.RedisUtil;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;
import ch.ethz.ast.gdblancer.redis.schema.RedisType;

import java.util.Map;

public class RedisDeleteGenerator extends CypherDeleteGenerator<RedisType> {

    public RedisDeleteGenerator(Schema<RedisType> schema) {
        super(schema);
    }

    public static RedisQuery deleteNodes(Schema<RedisType> schema) {
        RedisDeleteGenerator generator = new RedisDeleteGenerator(schema);
        generator.generateDelete();

        ExpectedErrors errors = new ExpectedErrors();

        RedisUtil.addFunctionErrors(errors);
        RedisUtil.addArithmeticErrors(errors);

        return new RedisQuery(generator.query.toString(), errors);
    }

    @Override
    protected String generateWhereClause(Entity<RedisType> entity) {
        return CypherVisitor.asString(RedisExpressionGenerator.generateExpression(Map.of("n", entity), RedisType.BOOLEAN));
    }

    @Override
    protected void onNonDetachDelete() {}

}