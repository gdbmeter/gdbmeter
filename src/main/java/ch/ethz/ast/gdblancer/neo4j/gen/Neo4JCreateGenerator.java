package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.cypher.gen.CypherCreateGenerator;
import ch.ethz.ast.gdblancer.cypher.gen.CypherPropertyGenerator;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.common.schema.Entity;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.neo4j.Neo4JUtil;
import ch.ethz.ast.gdblancer.neo4j.schema.Neo4JType;

import java.util.Map;

public class Neo4JCreateGenerator extends CypherCreateGenerator<Neo4JType> {

    public Neo4JCreateGenerator(Schema<Neo4JType> schema) {
        super(schema);
    }

    @Override
    protected CypherPropertyGenerator getPropertyGenerator(Entity<Neo4JType> entity, Map<String, Entity<Neo4JType>> variables) {
        return new Neo4JPropertyGenerator(entity, variables);
    }

    public static Neo4JQuery createEntities(Schema<Neo4JType> schema) {
        Neo4JCreateGenerator generator = new Neo4JCreateGenerator(schema);

        ExpectedErrors errors = new ExpectedErrors();
        Neo4JUtil.addRegexErrors(errors);
        Neo4JUtil.addArithmeticErrors(errors);
        Neo4JUtil.addFunctionErrors(errors);

        generator.generateCreate();
        return new Neo4JQuery(generator.query.toString(), errors);
    }

}
