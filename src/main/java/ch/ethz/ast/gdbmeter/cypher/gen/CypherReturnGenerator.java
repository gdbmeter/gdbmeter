package ch.ethz.ast.gdbmeter.cypher.gen;

import ch.ethz.ast.gdbmeter.cypher.CypherUtil;
import ch.ethz.ast.gdbmeter.common.schema.Entity;
import ch.ethz.ast.gdbmeter.util.Randomization;

import java.util.Map;

public class CypherReturnGenerator<T> {

    private final StringBuilder query = new StringBuilder();
    private final Map<String, Entity<T>> entities;

    private CypherReturnGenerator(Map<String, Entity<T>> entities) {
        this.entities = entities;
    }

    public static <E> String returnEntities(Map<String, Entity<E>> entities) {
        CypherReturnGenerator<E> generator = new CypherReturnGenerator<>(entities);
        generator.generateReturn();

        if (Randomization.getBoolean()) {
            generator.generateOrderBy();
        }

        if (Randomization.getBoolean()) {
            generator.generateLimit();
        }

        return generator.query.toString();
    }

    private void generateReturn() {
        if (Randomization.getBoolean()) {
            query.append("RETURN * ");
            return;
        }

        query.append("RETURN ");

        String separator = "";

        for (String variable : Randomization.nonEmptySubset(entities.keySet())) {
            query.append(separator);
            query.append(variable);

            if (Randomization.getBoolean()) {
                Entity<T> entity = entities.get(variable);
                String property = Randomization.fromSet(entity.getAvailableProperties().keySet());

                query.append(".");
                query.append(property);
            }

            query.append(" ");

            if (Randomization.getBoolean()) {
                query.append(String.format("AS %s ", CypherUtil.generateValidName()));
            }

            separator = ", ";
        }
    }

    private void generateOrderBy() {
        query.append("ORDER BY ");
        String separator = "";

        for (String variable : Randomization.nonEmptySubset(entities.keySet())) {
            query.append(separator);
            query.append(variable);

            if (Randomization.getBoolean()) {
                Entity<T> entity = entities.get(variable);
                String property = Randomization.fromSet(entity.getAvailableProperties().keySet());

                query.append(".");
                query.append(property);
            }

            query.append(" ");

            if (Randomization.getBoolean()) {
                if (Randomization.getBoolean()) {
                    query.append("DESC ");
                } else {
                    query.append("DESCENDING ");
                }
            }

            separator = ", ";
        }
    }

    private void generateLimit() {
        query.append(" LIMIT ");
        query.append(Randomization.getPositiveInteger());
    }

}
