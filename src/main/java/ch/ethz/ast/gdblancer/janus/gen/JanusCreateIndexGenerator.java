package ch.ethz.ast.gdblancer.janus.gen;

import ch.ethz.ast.gdblancer.common.schema.Index;
import ch.ethz.ast.gdblancer.common.schema.Schema;
import ch.ethz.ast.gdblancer.janus.query.JanusCreateIndexQuery;
import ch.ethz.ast.gdblancer.janus.query.JanusQueryAdapter;
import ch.ethz.ast.gdblancer.janus.schema.JanusType;
import ch.ethz.ast.gdblancer.util.Randomization;

import java.util.Map;
import java.util.Set;

public class JanusCreateIndexGenerator {

    public static JanusQueryAdapter createIndex(Schema<JanusType> schema) {
        String label;
        Set<String> properties;

        while (true) {
            Index index = schema.generateRandomNodeIndex();
            label = index.getLabel();
            properties = index.getPropertyNames();

            Map<String, JanusType> typeMap = schema.getEntityByLabel(label).getAvailableProperties();

            // Make sure that no character property is to be indexed.
            // See: https://github.com/JanusGraph/janusgraph/discussions/3144
            if (properties.stream().noneMatch(s -> typeMap.get(s).equals(JanusType.CHARACTER))) {
                break;
            }

        }

        String indexName = schema.generateRandomIndexName(JanusCreateIndexGenerator::generateValidLuceneName);

        return new JanusCreateIndexQuery(label, properties, indexName);
    }

    private static final Set<String> KEYWORDS = Set.of(
            "KEY", "VERTEX", "EDGE", "ELEMENT", "PROPERTY", "LABEL"
    );

    private static String generateValidLuceneName() {
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String candidate;

        do {
            candidate = Randomization.getCharacterFromAlphabet(alphabet) +
                    Randomization.getStringOfAlphabet(alphabet);
        } while (KEYWORDS.contains(candidate.toUpperCase()));

        return candidate;
    }

}
