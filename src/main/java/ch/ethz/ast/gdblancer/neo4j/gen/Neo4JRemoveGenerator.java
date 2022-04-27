package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBEntity;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.neo4j.gen.util.Neo4JDBUtil;
import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JRemoveGenerator {

    private final Neo4JDBSchema schema;
    private final StringBuilder query = new StringBuilder();
    private final ExpectedErrors errors = new ExpectedErrors();

    public Neo4JRemoveGenerator(Neo4JDBSchema schema) {
        this.schema = schema;
    }

    public static Neo4JQuery removeProperties(Neo4JDBSchema schema) {
        return new Neo4JRemoveGenerator(schema).generateRemove();
    }

    // TODO: Support REMOVE based on conditions
    // TODO: Support REMOVE on nodes with different labels
    // TODO: Support REMOVE of multiple properties
    // TODO: Add RETURN clause
    private Neo4JQuery generateRemove() {
        Neo4JDBUtil.addRegexErrors(errors);
        Neo4JDBUtil.addArithmeticErrors(errors);
        Neo4JDBUtil.addFunctionErrors(errors);

        String label = schema.getRandomLabel();
        Neo4JDBEntity entity = schema.getEntityByLabel(label);

        query.append(String.format("MATCH (n:%s ", label));
        query.append(Neo4JPropertyGenerator.generatePropertyQuery(entity));
        query.append(") ");

        String property = Randomization.fromSet(entity.getAvailableProperties().keySet());
        query.append(String.format("REMOVE n.%s ", property));

        return new Neo4JQuery(query.toString(), errors);
    }


}
