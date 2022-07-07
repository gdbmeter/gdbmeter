package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.Query;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Neo4JShowGeneratorTests extends Neo4JSchemaGenerator {

    @Test
    void testShowFunctions() {
        Query<?> query = Neo4JShowFunctionsGenerator.showFunctions(makeSchema());

        assertNotNull(query);
        assertTrue(query.getQuery().startsWith("SHOW "));
        assertTrue(query.getQuery().contains("FUNCTION"));
    }

    @Test
    void testShowProcedures() {
        Query<?> query = Neo4JShowProceduresGenerator.showProcedures(makeSchema());

        assertNotNull(query);
        assertTrue(query.getQuery().startsWith("SHOW PROCEDURE"));
    }

    @Test
    void testShowTransactions() {
        Query<?> query = Neo4JShowTransactionsGenerator.showTransactions(makeSchema());

        assertNotNull(query);
        assertTrue(query.getQuery().startsWith("SHOW TRANSACTION"));
    }

}
