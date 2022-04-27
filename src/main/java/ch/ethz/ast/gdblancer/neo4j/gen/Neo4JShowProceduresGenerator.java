package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JShowProceduresGenerator {

    private final StringBuilder query = new StringBuilder();

    public static Neo4JQuery showProcedures(Neo4JDBSchema ignored) {
        return new Neo4JShowProceduresGenerator().generateShowProcedures();
    }

    private Neo4JQuery generateShowProcedures() {
        query.append("SHOW PROCEDURE");

        if (Randomization.getBoolean()) {
            query.append("S");
        }

        return new Neo4JQuery(query.toString());
    }

}
