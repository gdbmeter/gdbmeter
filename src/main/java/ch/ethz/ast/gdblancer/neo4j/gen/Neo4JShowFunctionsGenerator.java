package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.cypher.schema.CypherSchema;
import ch.ethz.ast.gdblancer.neo4j.Neo4JUtil;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JShowFunctionsGenerator {

    private final CypherSchema schema;
    private final StringBuilder query = new StringBuilder();

    public Neo4JShowFunctionsGenerator(CypherSchema schema) {
        this.schema = schema;
    }

    public static Neo4JQuery showFunctions(CypherSchema schema) {
        return new Neo4JShowFunctionsGenerator(schema).generateShowFunctions();
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

    private Neo4JQuery generateDropIndex() {
        // Drop a non-existing index
        if (Randomization.smallBiasProbability()) {
            query.append(String.format("DROP INDEX %s IF EXISTS", Neo4JUtil.generateValidName()));
            return new Neo4JQuery(query.toString());
        }

        if (!schema.hasIndices()) {
            throw new IgnoreMeException();
        }

        query.append(String.format("DROP INDEX %s", schema.getRandomIndex()));

        if (Randomization.getBoolean()) {
            query.append(" IF EXISTS");
        }

        return new Neo4JQuery(query.toString());
    }

}
