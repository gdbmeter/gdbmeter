package ch.ethz.ast.gdbmeter.neo4j;

public class Neo4JBugs {

    // https://github.com/neo4j/neo4j/issues/12861
    public static final boolean bug12861 = true;

    // https://github.com/neo4j/neo4j/issues/12869
    public static final boolean bug12869 = true;

    // https://github.com/neo4j/neo4j/issues/128278
    public static final boolean bug12878 = true;

    // https://github.com/neo4j/neo4j/issues/12911
    public static final boolean bug12911 = true;

    static public class PartitionOracleSpecific {

        // https://github.com/neo4j/neo4j/issues/12883
        public static boolean bug12883 = false;

        // https://github.com/neo4j/neo4j/issues/12884
        public static boolean bug12884 = false;

        public static void enableAll() {
            bug12883 = true;
            bug12884 = true;
        }

        public static void disableAll() {
            bug12883 = false;
            bug12884 = false;
        }

    }

}
