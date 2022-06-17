package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.cypher.gen.CypherCreateGenerator;
import ch.ethz.ast.gdblancer.cypher.gen.CypherPropertyGenerator;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.cypher.schema.CypherEntity;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.neo4j.Neo4JUtil;

import java.util.Map;

public class Neo4JCreateGenerator extends CypherCreateGenerator {

    public Neo4JCreateGenerator(CypherSchema schema) {
        super(schema);
    }

    @Override
    protected CypherPropertyGenerator getPropertyGenerator(CypherEntity entity, Map<String, CypherEntity> variables) {
        return new Neo4JPropertyGenerator(entity, variables);
    }

    public static Neo4JQuery createEntities(CypherSchema schema) {
        Neo4JCreateGenerator generator = new Neo4JCreateGenerator(schema);

        ExpectedErrors errors = new ExpectedErrors();
        Neo4JUtil.addRegexErrors(errors);
        Neo4JUtil.addArithmeticErrors(errors);
        Neo4JUtil.addFunctionErrors(errors);

        generator.generateCreate();
        return new Neo4JQuery(generator.query.toString(), errors);
    }

}
