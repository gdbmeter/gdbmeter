package ch.ethz.ast.gdbmeter.janus.gen;

import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.janus.schema.JanusType;

import java.util.Set;

class JanusSchemaGenerator {

    Schema<JanusType> makeSchema() {
        return Schema.generateRandomSchema(Set.of(JanusType.values()));
    }

}
