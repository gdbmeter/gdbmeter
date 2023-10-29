![GDBMeter logo](gdbmeter.png)

# GDBMeter

[![Java CI](https://github.com/InverseIntegral/gdbmeter/actions/workflows/gradle.yml/badge.svg)](https://github.com/InverseIntegral/gdbmeter/actions/workflows/gradle.yml)

GDBMeter (Graph Database Metamorphic Tester) is a tool that automatically tests Graph Database Management Systems (GDBMS).
It tries to find bugs that cause the GDBMS to fetch an incorrect result set.

## Getting Started

Required Software:

- [JDK 11](https://www.oracle.com/java/technologies/downloads/)
- [Gradle 8.0.2](https://gradle.org/) which we provide bundled with our source code
- The graph database you want to test ([Neo4J](https://neo4j.com/) and [JanusGraph](https://janusgraph.org/) are both embedded into the JAR for ease of use)
    - We recommend testing [RedisGraph](https://redis.io/docs/stack/graph/) via a Docker container

The following commands clone the repository, build a JAR file and start GDBLancer:
```
git clone https://github.com/InverseIntegral/gdbmeter.git
cd gdbmeter
./gradlew clean assemble
cd build/libs
java -jar gdbmeter-*.jar -db neo4j -o partition
```
This will run GDBMeter until a bug has been found.

## Testing Approach

Our testing approach, called Predicate Partitioning, is based on the idea that we can partition a result set through
a predicate into disjoint subsets. Combining these subsets should again give the complete original result set. This
approach has been successfully applied to SQL databases and has been termed [Ternary Logic Partitioning
(TLP)](https://www.manuelrigger.at/preprints/TLP.pdf).

## Supported GDBMS

| **GDBMS**                                              | Tested Version | **Status** | **Description**                                                                                                                           |
|--------------------------------------------------------|----------------|------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| [Neo4J](https://github.com/neo4j/neo4j)                | 5.1.0          | Working    | Reference implementation for other GDBMS. This implementation is based on the common Cypher logic.                                        |
| [RedisGraph](https://github.com/RedisGraph/RedisGraph) | 2.8.13         | Working    | This implementation is based on the common Cypher logic. Running this implementation will most likely uncover more unreported logic bugs. |
| [JanusGraph](https://github.com/JanusGraph/janusgraph) | 0.6.2          | Working    | Supports a (small) subset of the Gremlin query language. Currently, only the inmemory version with the lucene index backend are tested.   |

## Using GDBMeter

### Logs

GDBMeter generates logs in the `logs` folder. Every individual query is logged and for JanusGraph the schema is serialized as part of the log.
If GDBMeter finds a bug, the log can be used to reproduce the bug.

### Reproducing a Bug

To reproduce a bug simply find the last logged message of the form `Finished iteration, closing database`. And remove everything above it.
This leaves only the queries that were run during the latest iteration. Place these in a file called `replay` in the `logs` folder.
Re-running GDBMeter with the option `--replay` or `-r` then runs only those queries that are present in `replay`.
At this point a manual reduction is necessary. Usually, the best way to do this is to remove part of the queries and see if the bug still occurs.
Continue this until a minimal example is left. We plan on adding support for an automatic reduction test based on [Delta Debugging](https://en.wikipedia.org/wiki/Delta_debugging).

### Found Bugs

In case you found a bug using GDBMeter we would appreciate it if you mention our project when reporting the bug.
We would, of course, be very happy if you use GDBMeter or if you would like to contribute.

## Alternative Testing Approaches

GDBMeter also supports other, less successful, testing approaches:

| **Name**         | **Description**                                                                                                          |
|------------------|--------------------------------------------------------------------------------------------------------------------------|
| Empty Result     | A query that has an empty result set should return an empty result whenever no new nodes are added.                      |
| Non-Empty Result | A query that has a non-empty result set should remain unchanged as long as we don't remove any nodes of said result set. |
| Refinement       | Matching on (a subset of) the exact values of a previous query should return (a superset of) the previous result set.    |

