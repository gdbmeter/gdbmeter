package ch.ethz.ast.gdbmeter.redis.gen;

import ch.ethz.ast.gdbmeter.common.ExpectedErrors;
import ch.ethz.ast.gdbmeter.cypher.ast.CypherExpression;
import ch.ethz.ast.gdbmeter.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdbmeter.cypher.gen.CypherSetGenerator;
import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.redis.RedisQuery;
import ch.ethz.ast.gdbmeter.redis.RedisUtil;
import ch.ethz.ast.gdbmeter.redis.ast.RedisExpressionGenerator;
import ch.ethz.ast.gdbmeter.redis.schema.RedisType;

import java.util.Map;

public class RedisSetGenerator extends CypherSetGenerator<RedisType> {

    public RedisSetGenerator(Schema<RedisType> schema) {
        super(schema);
    }

    public static RedisQuery setProperties(Schema<RedisType> schema) {
        ExpectedErrors errors = new ExpectedErrors();

        RedisUtil.addFunctionErrors(errors);
        RedisUtil.addArithmeticErrors(errors);


        RedisSetGenerator generator = new RedisSetGenerator(schema);
        generator.generateSet();
        return new RedisQuery(generator.query.toString(), errors);
    }

    @Override
    protected String generateWhereClause(Entity<RedisType> entity) {
        return CypherVisitor.asString(RedisExpressionGenerator.generateExpression(Map.of("n", entity), RedisType.BOOLEAN));
    }

    @Override
    protected CypherExpression generateConstant(RedisType type) {
        return RedisExpressionGenerator.generateConstant(type);
    }

    @Override
    protected CypherExpression generateExpression(RedisType type) {
        return RedisExpressionGenerator.generateExpression(type);
    }

}
