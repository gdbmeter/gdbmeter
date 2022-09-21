package ch.ethz.ast.gdbmeter.neo4j.gen;

import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.neo4j.Neo4JQuery;
import ch.ethz.ast.gdbmeter.neo4j.schema.Neo4JType;
import ch.ethz.ast.gdbmeter.util.Randomization;

public class Neo4JShowFunctionsGenerator {

    private final StringBuilder query = new StringBuilder();

    public static Neo4JQuery showFunctions(Schema<Neo4JType> ignored) {
        return new Neo4JShowFunctionsGenerator().generateShowFunctions();
    }

    private enum FunctionFilterType {
        ALL,
        BUILT_IN,
        USER_DEFINED,
        NONE
    }

    private Neo4JQuery generateShowFunctions() {
        query.append("SHOW ");

        switch (Randomization.fromOptions(FunctionFilterType.values())) {
            case ALL:
                query.append("ALL ");
                break;
            case BUILT_IN:
                query.append("BUILT IN ");
                break;
            case USER_DEFINED:
                query.append("USER DEFINED ");
                break;
            case NONE:
                break;
            default:
                throw new AssertionError();
        }

        query.append("FUNCTION");

        if (Randomization.getBoolean()) {
            query.append("S");
        }

        return new Neo4JQuery(query.toString());
    }

}
