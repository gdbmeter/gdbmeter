package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBEntity;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JType;
import ch.ethz.ast.gdblancer.neo4j.gen.util.Neo4JDBUtil;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Set;

public class Neo4JSetGenerator {

    private final Neo4JDBSchema schema;
    private final StringBuilder query = new StringBuilder();
    private final ExpectedErrors errors = new ExpectedErrors();

    public Neo4JSetGenerator(Neo4JDBSchema schema) {
        this.schema = schema;
    }

    public static Query setProperties(Neo4JDBSchema schema) {
        return new Neo4JSetGenerator(schema).generateSet();
    }

    // TODO: Support SET based on conditions
    // TODO: Support SET on nodes with different labels
    // TODO: Support SET of multiple properties
    // TODO: Add RETURN clause
    private Query generateSet() {
        Neo4JDBUtil.addRegexErrors(errors);
        Neo4JDBUtil.addArithmeticErrors(errors);
        Neo4JDBUtil.addFunctionErrors(errors);

        String label = schema.getRandomLabel();
        Neo4JDBEntity entity = schema.getEntityByLabel(label);

        query.append(String.format("MATCH (n:%s ", label));
        query.append(Neo4JPropertyGenerator.generatePropertyQuery(entity));
        query.append(") ");

        Set<String> properties = entity.getAvailableProperties().keySet();
        String property = Randomization.fromSet(properties);
        Neo4JType type = entity.getAvailableProperties().get(property);

        query.append(String.format("SET n.%s = %s", property, Neo4JPropertyGenerator.generateRandomValue(type)));

        return new Query(query.toString(), errors);
    }


}
