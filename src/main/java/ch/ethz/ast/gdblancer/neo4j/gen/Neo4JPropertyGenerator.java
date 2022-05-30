package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.neo4j.gen.ast.Neo4JExpression;
import ch.ethz.ast.gdblancer.neo4j.gen.ast.Neo4JExpressionGenerator;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBEntity;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JType;

import java.util.Map;

public class Neo4JPropertyGenerator extends CypherPropertyGenerator {

    protected Neo4JPropertyGenerator(Neo4JDBEntity entity) {
        super(entity);
    }

    protected Neo4JPropertyGenerator(Neo4JDBEntity entity, Map<String, Neo4JDBEntity> variables) {
        super(entity, variables);
    }

    @Override
    protected Neo4JExpression generateConstant(Neo4JType type) {
        return Neo4JExpressionGenerator.generateConstant(type);
    }

    @Override
    protected Neo4JExpression generateExpression(Map<String, Neo4JDBEntity> variables, Neo4JType type) {
        return Neo4JExpressionGenerator.generateExpression(variables, type);
    }

}
