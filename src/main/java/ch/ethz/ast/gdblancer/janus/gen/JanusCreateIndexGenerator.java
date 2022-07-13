package ch.ethz.ast.gdblancer.janus.gen;

import ch.ethz.ast.gdblancer.common.schema.Index;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.janus.query.JanusCreateIndexQuery;
import ch.ethz.ast.gdblancer.janus.query.JanusQueryAdapter;
import ch.ethz.ast.gdblancer.janus.schema.JanusType;

import java.util.Set;

public class JanusCreateIndexGenerator {

    public static JanusQueryAdapter createIndex(Schema<JanusType> schema) {
        Index index = schema.generateRandomNodeIndex();
        String label = index.getLabel();
        Set<String> properties = index.getPropertyNames();
        String indexName = schema.generateRandomIndexName();

        return new JanusCreateIndexQuery(label, properties, indexName);
    }

}
