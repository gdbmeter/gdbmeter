package ch.ethz.ast.gdbmeter.neo4j.gen;

import ch.ethz.ast.gdbmeter.neo4j.Neo4JQuery;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.neo4j.schema.Neo4JType;
import ch.ethz.ast.gdbmeter.util.Randomization;

public class Neo4JShowProceduresGenerator {

    private final StringBuilder query = new StringBuilder();

    public static Neo4JQuery showProcedures(Schema<Neo4JType> ignored) {
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
