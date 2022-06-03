package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.cypher.gen.CypherCreateIndexGenerator;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;

public class Neo4JCreateIndexGenerator extends CypherCreateIndexGenerator {

    public Neo4JCreateIndexGenerator(Neo4JDBSchema schema) {
        super(schema);
    }

    public static Neo4JQuery createIndex(Neo4JDBSchema schema) {
        Neo4JCreateIndexGenerator generator = new Neo4JCreateIndexGenerator(schema);
        generator.generateCreateIndex();

        ExpectedErrors errors = new ExpectedErrors();
        errors.add("There already exists an index");
        errors.add("An equivalent index already exists");

        return new Neo4JQuery(generator.query.toString(), errors, true);
    }

}
