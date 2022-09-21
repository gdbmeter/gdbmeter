package ch.ethz.ast.gdbmeter.neo4j.gen;

import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.neo4j.schema.Neo4JType;

import java.util.Set;

class Neo4JSchemaGenerator {

    Schema<Neo4JType> makeSchema() {
        return Schema.generateRandomSchema(Set.of(Neo4JType.values()));
    }

}
