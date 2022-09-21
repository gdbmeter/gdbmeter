package ch.ethz.ast.gdbmeter.redis.oracle;

import ch.ethz.ast.gdbmeter.common.ExpectedErrors;
import ch.ethz.ast.gdbmeter.common.GlobalState;
import ch.ethz.ast.gdbmeter.common.Query;
import ch.ethz.ast.gdbmeter.cypher.ast.CypherVisitor;
import ch.ethz.ast.gdbmeter.cypher.oracle.CypherEmptyResultOracle;
import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.redis.RedisConnection;
import ch.ethz.ast.gdbmeter.redis.RedisQuery;
import ch.ethz.ast.gdbmeter.redis.RedisUtil;
import ch.ethz.ast.gdbmeter.redis.ast.RedisExpressionGenerator;
import ch.ethz.ast.gdbmeter.redis.schema.RedisType;

import java.util.Map;

public class RedisEmptyResultOracle extends CypherEmptyResultOracle<RedisConnection, RedisType> {

    public RedisEmptyResultOracle(GlobalState<RedisConnection> state, Schema<RedisType> schema) {
        super(state, schema);
    }

    @Override
    protected Query<RedisConnection> getIdQuery() {
        return new RedisQuery("MATCH (n) RETURN id(n)");
    }

    @Override
    protected Query<RedisConnection> getInitialQuery(String label, Entity<RedisType> entity) {
        ExpectedErrors errors = new ExpectedErrors();
        RedisUtil.addFunctionErrors(errors);
        RedisUtil.addArithmeticErrors(errors);

        String query = String.format("MATCH (n:%s) WHERE %s RETURN n",
                label,
                CypherVisitor.asString(RedisExpressionGenerator.generateExpression(Map.of("n", entity), RedisType.BOOLEAN)));
        return new RedisQuery(query, errors);
    }

    @Override
    protected Query<RedisConnection> getDeleteQuery(Long id) {
        return new RedisQuery(String.format("MATCH (n) WHERE id(n) = %d DETACH DELETE n", id));
    }

}
