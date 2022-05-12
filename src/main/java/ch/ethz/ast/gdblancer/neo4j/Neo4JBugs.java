package ch.ethz.ast.gdblancer.neo4j;

public class Neo4JBugs {

    // https://github.com/neo4j/neo4j/issues/12861
    public static final boolean bug12861 = true;

    // https://github.com/neo4j/neo4j/issues/12866
    public static final boolean bug12866 = true;

    // https://github.com/neo4j/neo4j/issues/12869
    public static final boolean bug12869 = true;

    // https://github.com/neo4j/neo4j/issues/12877
    public static final boolean bug12877 = true;

    // https://github.com/neo4j/neo4j/issues/12879
    public static final boolean bug12879 = true;

    // https://github.com/neo4j/neo4j/issues/12878
    public static final boolean bug12878 = true;

    // https://github.com/neo4j/neo4j/issues/12880
    public static final boolean bug12880 = true;

    // https://github.com/neo4j/neo4j/issues/12881
    public static final boolean bug12881 = true;

    // TODO: Fina a way to make the enable and disable logic easier to handle for Main
    static public class PartitionOracleSpecific {

        // https://github.com/neo4j/neo4j/issues/12883
        public static boolean bug12883 = true;

        public static void enableAll() {
            bug12883 = true;
        }

        public static void disableAll() {
            bug12883 = false;
        }

    }

}
