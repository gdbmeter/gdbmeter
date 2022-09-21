package ch.ethz.ast.gdbmeter.redis.gen;

import ch.ethz.ast.gdbmeter.common.ExpectedErrors;
import ch.ethz.ast.gdbmeter.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdbmeter.cypher.gen.CypherRemoveGenerator;
import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.redis.RedisQuery;
import ch.ethz.ast.gdbmeter.redis.RedisUtil;
import ch.ethz.ast.gdbmeter.redis.ast.RedisExpressionGenerator;
import ch.ethz.ast.gdbmeter.redis.schema.RedisType;

import java.util.Map;

public class RedisRemoveGenerator extends CypherRemoveGenerator<RedisType>  {

    public RedisRemoveGenerator(Schema<RedisType> schema) {
        super(schema);
    }

    public static RedisQuery removeProperties(Schema<RedisType> schema) {
        RedisRemoveGenerator generator = new RedisRemoveGenerator(schema);
        generator.generateRemove();

        ExpectedErrors errors = new ExpectedErrors();
        RedisUtil.addFunctionErrors(errors);
        RedisUtil.addArithmeticErrors(errors);

        return new RedisQuery(generator.query.toString(), errors);
    }

    @Override
    protected String generateWhereClause(Entity<RedisType> entity) {
        return CypherVisitor.asString(RedisExpressionGenerator.generateExpression(Map.of("n", entity), RedisType.BOOLEAN));
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
