package ch.ethz.ast.gdblancer.neo4j.gen;

public class Neo4JLabelGenerator {

    public static String generateRandomLabel() {
        return ":" + Neo4JGraphGenerator.generateValidName();
    }

}
