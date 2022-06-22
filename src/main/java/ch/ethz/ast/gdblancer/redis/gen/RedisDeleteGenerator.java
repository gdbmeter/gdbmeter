package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdblancer.cypher.gen.CypherDeleteGenerator;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.cypher.schema.CypherType;
import ch.ethz.ast.gdblancer.redis.RedisQuery;
import ch.ethz.ast.gdblancer.redis.RedisUtil;
import ch.ethz.ast.gdblancer.redis.ast.RedisExpressionGenerator;

import java.util.Map;

public class RedisDeleteGenerator extends CypherDeleteGenerator {

    public RedisDeleteGenerator(CypherSchema schema) {
        super(schema);
    }

    public static RedisQuery deleteNodes(CypherSchema schema) {
        RedisDeleteGenerator generator = new RedisDeleteGenerator(schema);
        generator.generateDelete();

        ExpectedErrors errors = new ExpectedErrors();

        RedisUtil.addFunctionErrors(errors);
        RedisUtil.addArithmeticErrors(errors);

        return new RedisQuery(generator.query.toString(), errors);
    }

    @Override
    protected String generateWhereClause(CypherEntity entity) {
        return CypherVisitor.asString(RedisExpressionGenerator.generateExpression(Map.of("n", entity), CypherType.BOOLEAN));
    }

    @Override
    protected void onNonDetachDelete() {}

}