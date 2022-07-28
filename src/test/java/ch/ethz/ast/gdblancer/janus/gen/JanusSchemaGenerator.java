package ch.ethz.ast.gdblancer.janus.gen;

import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.janus.schema.JanusType;

import java.util.Set;

class JanusSchemaGenerator {

    Schema<JanusType> makeSchema() {
        return Schema.generateRandomSchema(Set.of(JanusType.values()));
    }

}
