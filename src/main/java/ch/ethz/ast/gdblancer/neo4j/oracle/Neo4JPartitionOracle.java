package ch.ethz.ast.gdblancer.neo4j.oracle;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.GlobalState;
import ch.ethz.ast.gdblancer.common.Oracle;
import ch.ethz.ast.gdblancer.neo4j.Neo4JConnection;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.neo4j.gen.Neo4JPropertyGenerator;
import ch.ethz.ast.gdblancer.neo4j.gen.ast.Neo4JExpression;
import ch.ethz.ast.gdblancer.neo4j.gen.ast.Neo4JExpressionGenerator;
import ch.ethz.ast.gdblancer.neo4j.gen.ast.Neo4JPrefixOperation;
import ch.ethz.ast.gdblancer.neo4j.gen.ast.Neo4JVisitor;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBEntity;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JType;
import ch.ethz.ast.gdblancer.neo4j.gen.util.Neo4JDBUtil;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Neo4JPartitionOracle implements Oracle {

    private final GlobalState<Neo4JConnection> state;
    private final Neo4JDBSchema schema;

    public Neo4JPartitionOracle(GlobalState<Neo4JConnection> state, Neo4JDBSchema schema) {
        this.state = state;
        this.schema = schema;
    }

    @Override
    public void check() {
        ExpectedErrors errors = new ExpectedErrors();

        Neo4JDBUtil.addRegexErrors(errors);
        Neo4JDBUtil.addArithmeticErrors(errors);
        Neo4JDBUtil.addFunctionErrors(errors);

        String label = schema.getRandomLabel();
        Neo4JDBEntity entity = schema.getEntityByLabel(label);

        Long count = (Long) new Neo4JQuery(String.format("MATCH (n:%s) RETURN COUNT(n)", label)).executeAndGet(state).get(0).get("COUNT(n)");

        StringBuilder query = new StringBuilder();
        query.append(String.format("MATCH (n:%s)", label));
        query.append(" WHERE ");

        Neo4JExpression whereCondition = Neo4JExpressionGenerator.generateExpression(Map.of("n", entity), Neo4JType.BOOLEAN);
        query.append(Neo4JVisitor.asString(whereCondition));
        query.append(" RETURN COUNT(n)");

        Neo4JQuery initialQuery = new Neo4JQuery(query.toString(), errors);
        Long first = (Long) initialQuery.executeAndGet(state).get(0).get("COUNT(n)");

        query = new StringBuilder();
        query.append(String.format("MATCH (n:%s)", label));
        query.append(" WHERE ");

        query.append(Neo4JVisitor.asString(new Neo4JPrefixOperation(whereCondition, Neo4JPrefixOperation.PrefixOperator.NOT)));
        query.append(" RETURN COUNT(n)");

        Neo4JQuery secondQuery = new Neo4JQuery(query.toString(), errors);
        Long second = (Long) secondQuery.executeAndGet(state).get(0).get("COUNT(n)");

        if (first + second != count) {
            throw new AssertionError(String.format("%d + %d is not equal to %d", first, second, count));
        }
    }
}
