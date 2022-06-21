package ch.ethz.ast.gdblancer.neo4j.ast;

import ch.ethz.ast.gdblancer.cypher.ast.CypherConstant;

import java.util.Map;

public class Neo4JDurationConstant extends CypherConstant {

    private String value;
    private Map<String, Long> datePart;
    private Map<String, Long> timePart;

    public Neo4JDurationConstant(Map<String, Long> datePart, Map<String, Long> timePart) {
        this.datePart = datePart;
        this.timePart = timePart;

        if (datePart.isEmpty() && timePart.isEmpty()) {
            throw new IllegalArgumentException("Cannot create empty duration");
        }
    }

    public Neo4JDurationConstant(String value) {
        this.value = value;
    }

    // We support the format: P[nY][nM][nW][nD][T[nH][nM][nS]]
    @Override
    public String getTextRepresentation() {
        if (value != null) {
            return String.format("duration('%s')", value);
        }

        StringBuilder representation = new StringBuilder();
        representation.append("duration('P");

        for (String current : datePart.keySet()) {
            representation.append(String.format("%d%s", datePart.get(current), current));
        }

        // Only 'T' alone is not considered valid
        if (!timePart.isEmpty()) {
            representation.append("T");

            for (String current : timePart.keySet()) {
                representation.append(String.format("%d%s", timePart.get(current), current));
            }
        }

        representation.append("')");
        return representation.toString();
    }
}
