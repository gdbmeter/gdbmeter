package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.cypher.gen.CypherCreateGenerator;
import ch.ethz.ast.gdblancer.cypher.gen.CypherPropertyGenerator;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBEntity;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.neo4j.gen.util.Neo4JDBUtil;

import java.util.Map;

public class Neo4JCreateGenerator extends CypherCreateGenerator {

    public Neo4JCreateGenerator(Neo4JDBSchema schema) {
        super(schema);
    }

    @Override
    protected CypherPropertyGenerator getPropertyGenerator(Neo4JDBEntity entity, Map<String, Neo4JDBEntity> variables) {
        return new Neo4JPropertyGenerator(entity, variables);
    }

    public static Neo4JQuery createEntities(Neo4JDBSchema schema) {
        Neo4JCreateGenerator generator = new Neo4JCreateGenerator(schema);

        ExpectedErrors errors = new ExpectedErrors();
        Neo4JDBUtil.addRegexErrors(errors);
        Neo4JDBUtil.addArithmeticErrors(errors);
        Neo4JDBUtil.addFunctionErrors(errors);

        generator.generateCreate();
        return new Neo4JQuery(generator.query.toString(), errors);
    }

}
