package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JShowProceduresGenerator {

    private final StringBuilder query = new StringBuilder();

    public static Query showProcedures(Neo4JDBSchema ignored) {
        return new Neo4JShowProceduresGenerator().generateShowProcedures();
    }

    private Query generateShowProcedures() {
        query.append("SHOW PROCEDURE");

        if (Randomization.getBoolean()) {
            query.append("S");
        }

        return new Query(query.toString());
    }

}
