package ch.ethz.ast.gdblancer.neo4j.gen;

import ch.ethz.ast.gdblancer.common.ExpectedErrors;
import ch.ethz.ast.gdblancer.cypher.gen.CypherCreateIndexGenerator;
import ch.ethz.ast.gdblancer.common.schema.Index;
import ch.ethz.ast.gdblancer.neo4j.Neo4JQuery;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.neo4j.schema.Neo4JType;
import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JCreateIndexGenerator extends CypherCreateIndexGenerator<Neo4JType> {

    public Neo4JCreateIndexGenerator(Schema<Neo4JType> schema) {
        super(schema, Neo4JType.STRING);
    }

    @Override
    protected void generateNodeIndex(Index index) {
        generateIndex("FOR (n:%s) ", index);
    }

    @Override
    protected void generateRelationshipIndex(Index index) {
        generateIndex("FOR ()-[n:%s]-() ", index);
    }

    @Override
    protected void generateTextIndex(Index index) {
        query.append("CREATE TEXT INDEX ");
        query.append(schema.generateRandomIndexName());
        query.append(" ");

        // TODO: Maybe choose same name deliberately in this case?
        if (Randomization.getBoolean()) {
            query.append("IF NOT EXISTS ");
        }

        query.append(String.format("FOR (n:%s) ON (n.%s)", index.getLabel(), index.getPropertyNames().toArray()[0]));
    }

    private void generateIndex(String format, Index index) {
        query.append("CREATE ");

        if (Randomization.getBoolean()) {
            query.append("BTREE ");
        }

        query.append("INDEX ");

        // TODO: Maybe add support for unnamed indices
        query.append(schema.generateRandomIndexName());
        query.append(" ");

        // TODO: Maybe choose same name deliberately in this case?
        if (Randomization.getBoolean()) {
            query.append("IF NOT EXISTS ");
        }

        query.append(String.format(format, index.getLabel()));
        generateOnClause(index.getPropertyNames());
    }

    public static Neo4JQuery createIndex(Schema<Neo4JType> schema) {
        Neo4JCreateIndexGenerator generator = new Neo4JCreateIndexGenerator(schema);
        generator.generateCreateIndex();

        ExpectedErrors errors = new ExpectedErrors();
        errors.add("There already exists an index");
        errors.add("An equivalent index already exists");

        return new Neo4JQuery(generator.query.toString(), errors, true);
    }

}
