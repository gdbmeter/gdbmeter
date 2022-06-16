package ch.ethz.ast.gdblancer.redis.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.cypher.gen.CypherCreateGenerator;
import ch.ethz.ast.gdblancer.cypher.gen.CypherPropertyGenerator;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.redis.RedisQuery;
import ch.ethz.ast.gdblancer.redis.RedisUtil;

import java.util.Map;

public class RedisCreateGenerator extends CypherCreateGenerator {

    public RedisCreateGenerator(CypherSchema schema) {
        super(schema);
    }

    @Override
    protected CypherPropertyGenerator getPropertyGenerator(CypherEntity entity, Map<String, CypherEntity> variables) {
        return new RedisPropertyGenerator(entity, variables);
    }

    public static RedisQuery createEntities(CypherSchema schema) {
        RedisCreateGenerator generator = new RedisCreateGenerator(schema);
        generator.generateCreate();

        ExpectedErrors errors = new ExpectedErrors();
        RedisUtil.addFunctionErrors(errors);
        RedisUtil.addArithmeticErrors(errors);

        return new RedisQuery(generator.query.toString(), errors);
    }

}
