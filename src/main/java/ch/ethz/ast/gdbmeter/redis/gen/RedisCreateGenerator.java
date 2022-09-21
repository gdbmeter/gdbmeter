package ch.ethz.ast.gdbmeter.redis.gen;

import ch.ethz.ast.gdbmeter.common.ExpectedErrors;
import ch.ethz.ast.gdbmeter.cypher.gen.CypherCreateGenerator;
import ch.ethz.ast.gdbmeter.cypher.gen.CypherPropertyGenerator;
import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.redis.RedisQuery;
import ch.ethz.ast.gdbmeter.redis.RedisUtil;
import ch.ethz.ast.gdbmeter.redis.schema.RedisType;

import java.util.Map;

public class RedisCreateGenerator extends CypherCreateGenerator<RedisType> {

    public RedisCreateGenerator(Schema<RedisType> schema) {
        super(schema);
    }

    @Override
    protected CypherPropertyGenerator<RedisType> getPropertyGenerator(Entity<RedisType> entity, Map<String, Entity<RedisType>> variables) {
        return new RedisPropertyGenerator(entity, variables);
    }

    public static RedisQuery createEntities(Schema<RedisType> schema) {
        RedisCreateGenerator generator = new RedisCreateGenerator(schema);
        generator.generateCreate();

        ExpectedErrors errors = new ExpectedErrors();
        RedisUtil.addFunctionErrors(errors);
        RedisUtil.addArithmeticErrors(errors);

        return new RedisQuery(generator.query.toString(), errors);
    }

}
