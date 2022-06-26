package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.cypher.CypherUtil;
import ch.ethz.ast.gdblancer.common.schema.CypherSchema;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.util.IgnoreMeException;
import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JDropIndexGenerator {

    private final CypherSchema schema;
    private final StringBuilder query = new StringBuilder();

    public Neo4JDropIndexGenerator(CypherSchema schema) {
        this.schema = schema;
    }

    public static Neo4JQuery dropIndex(CypherSchema schema) {
        return new Neo4JDropIndexGenerator(schema).generateDropIndex();
    }

    private Neo4JQuery generateDropIndex() {
        // Drop a non-existing index
        if (Randomization.smallBiasProbability()) {
            query.append(String.format("DROP INDEX %s IF EXISTS", CypherUtil.generateValidName()));
            return new Neo4JQuery(query.toString());
        }

        if (!schema.hasIndices()) {
            throw new IgnoreMeException();
        }

        query.append(String.format("DROP INDEX %s", schema.getRandomIndex()));

        if (Randomization.getBoolean()) {
            query.append(" IF EXISTS");
        }

        return new Neo4JQuery(query.toString(), true);
    }

}
