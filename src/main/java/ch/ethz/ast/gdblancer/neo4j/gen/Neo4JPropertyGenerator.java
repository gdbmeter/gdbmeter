package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.cypher.gen.CypherPropertyGenerator;
import ch.ethz.ast.gdblancer.cypher.ast.CypherExpression;
import ch.ethz.ast.gdblancer.common.schema.CypherEntity;
import ch.ethz.ast.gdblancer.common.schema.CypherType;
import ch.ethz.ast.gdblancer.neo4j.ast.Neo4JExpressionGenerator;

import java.util.Map;

public class Neo4JPropertyGenerator extends CypherPropertyGenerator {

    protected Neo4JPropertyGenerator(CypherEntity entity) {
        super(entity);
    }

    protected Neo4JPropertyGenerator(CypherEntity entity, Map<String, CypherEntity> variables) {
        super(entity, variables);
    }

    @Override
    protected CypherExpression generateConstant(CypherType type) {
        return Neo4JExpressionGenerator.generateConstant(type);
    }

    @Override
    protected CypherExpression generateExpression(Map<String, CypherEntity> variables, CypherType type) {
        return Neo4JExpressionGenerator.generateExpression(variables, type);
    }

}
