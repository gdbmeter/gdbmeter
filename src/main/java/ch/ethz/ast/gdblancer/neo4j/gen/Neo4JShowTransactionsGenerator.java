package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.Query;
import ch.ethz.ast.gdblancer.neo4j.gen.schema.Neo4JDBSchema;
import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JShowTransactionsGenerator {

    private final StringBuilder query = new StringBuilder();

    public static Query showTransactions(Neo4JDBSchema ignored) {
        return new Neo4JShowTransactionsGenerator().generateShowTransactions();
    }

    private Query generateShowTransactions() {
        query.append("SHOW TRANSACTION");

        if (Randomization.getBoolean()) {
            query.append("S");
        }

        return new Query(query.toString());
    }

}
