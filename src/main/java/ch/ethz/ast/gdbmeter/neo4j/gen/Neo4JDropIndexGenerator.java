package ch.ethz.ast.gdbmeter.neo4j.gen;

import ch.ethz.ast.gdbmeter.cypher.CypherUtil;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.neo4j.Neo4JQuery;
import ch.ethz.ast.gdbmeter.neo4j.schema.Neo4JType;
import ch.ethz.ast.gdbmeter.util.IgnoreMeException;
import ch.ethz.ast.gdbmeter.util.Randomization;

public class Neo4JDropIndexGenerator {

    private final Schema<Neo4JType> schema;
    private final StringBuilder query = new StringBuilder();

    public Neo4JDropIndexGenerator(Schema<Neo4JType> schema) {
        this.schema = schema;
    }

    public static Neo4JQuery dropIndex(Schema<Neo4JType> schema) {
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
