package ch.ethz.ast.gdblancer.neo4j.gen.schema;

import ch.ethz.ast.gdblancer.util.Randomization;

public class Neo4JDBUtil {

    /**
     * A valid name begins with an alphabetic character and not with a number
     * Furthermore, a valid name does not contain symbols except for underscores
     */
    public static String generateValidName() {
        return Randomization.getCharacterFromAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")
                + Randomization.getStringOfAlphabet("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_");
    }

}
