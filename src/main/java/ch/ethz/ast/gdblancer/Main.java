package ch.ethz.ast.gdblancer;

import ch.ethz.ast.gdblancer.neo4j.Neo4JConnection;
import ch.ethz.ast.gdblancer.neo4j.gen.Neo4JGraphGenerator;
import ch.ethz.ast.gdblancer.util.Randomization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        while (true) {
            try (Neo4JConnection connection = new Neo4JConnection()) {
                Neo4JGraphGenerator generator = new Neo4JGraphGenerator();
                generator.generate(connection);
            } catch (Exception e) {
                LOGGER.error("Exception thrown: ", e);
                System.exit(0);
            }
        }
    }

}
