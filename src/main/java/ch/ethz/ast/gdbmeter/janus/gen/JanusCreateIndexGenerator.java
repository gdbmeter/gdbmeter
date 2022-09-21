package ch.ethz.ast.gdbmeter.janus.gen;

import ch.ethz.ast.gdbmeter.common.Query;
import ch.ethz.ast.gdbmeter.common.schema.Index;
import ch.ethz.ast.gdbmeter.common.schema.Schema;
import ch.ethz.ast.gdbmeter.janus.JanusBugs;
import ch.ethz.ast.gdbmeter.janus.JanusConnection;
import ch.ethz.ast.gdbmeter.janus.query.JanusCreateIndexQuery;
import ch.ethz.ast.gdbmeter.janus.schema.JanusType;
import ch.ethz.ast.gdbmeter.util.Randomization;

import java.util.Map;
import java.util.Set;

public class JanusCreateIndexGenerator {

    public static Query<JanusConnection> createIndex(Schema<JanusType> schema) {
        String label;
        Set<String> properties;
        boolean composite = Randomization.getBoolean();

        // The following ensures that there's no property of type char selected.
        // This is because Lucene indices don't support char types.
        while (true) {
            Index index = schema.generateRandomNodeIndex();
            label = index.getLabel();
            properties = index.getPropertyNames();

            if (composite || !JanusBugs.bug3144) {
                break;
            }

            Map<String, JanusType> typeMap = schema.getEntityByLabel(label).getAvailableProperties();

            if (properties.stream().noneMatch(s -> typeMap.get(s).equals(JanusType.CHARACTER))) {
                break;
            }
        }

        return new JanusCreateIndexQuery(label,
                properties,
                schema.generateRandomIndexName(JanusCreateIndexGenerator::generateValidLuceneName),
                composite);
    }

    // TODO: Check if this is necessary and if so create JanusUtil
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
