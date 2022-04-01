package ch.ethz.ast.gdblancer;

import ch.ethz.ast.gdblancer.neo4j.Neo4JConnection;
import ch.ethz.ast.gdblancer.neo4j.gen.Neo4JGraphGenerator;

public class Main {

    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            try (Neo4JConnection connection = new Neo4JConnection()) {
                Neo4JGraphGenerator generator = new Neo4JGraphGenerator();
                generator.generate(connection);
            }
        }
    }

}
