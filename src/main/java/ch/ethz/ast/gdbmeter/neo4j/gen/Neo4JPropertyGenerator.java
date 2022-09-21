package ch.ethz.ast.gdbmeter.neo4j.gen;

import ch.ethz.ast.gdbmeter.cypher.gen.CypherPropertyGenerator;
import ch.ethz.ast.gdbmeter.cypher.ast.CypherExpression;
import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.neo4j.ast.Neo4JExpressionGenerator;
import ch.ethz.ast.gdbmeter.neo4j.schema.Neo4JType;

import java.util.Map;

public class Neo4JPropertyGenerator extends CypherPropertyGenerator<Neo4JType>  {

    protected Neo4JPropertyGenerator(Entity<Neo4JType> entity) {
        super(entity);
    }

    protected Neo4JPropertyGenerator(Entity<Neo4JType> entity, Map<String, Entity<Neo4JType>> variables) {
        super(entity, variables);
    }

    @Override
    protected CypherExpression generateConstant(Neo4JType type) {
        return Neo4JExpressionGenerator.generateConstant(type);
    }

    @Override
    protected CypherExpression generateExpression(Map<String, Entity<Neo4JType>> variables, Neo4JType type) {
        return Neo4JExpressionGenerator.generateExpression(variables, type);
    }

}
