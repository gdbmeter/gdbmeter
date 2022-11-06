![GDBMeter logo](gdbmeter.png)
# GDBMeter

[![Test](https://github.com/InverseIntegral/gdbmeter/actions/workflows/maven.yml/badge.svg)](https://github.com/InverseIntegral/gdbmeter/actions/workflows/maven.yml)

GDBMeter (Graph Database Metamorphic Tester) is a tool that automatically tests Graph Database Management Systems (GDBMS).
It tries to find bugs that cause the GDBMS to fetch an incorrect result set.

## Getting Started

Required Software:
- Java 11
- Maven
- The graph database you want to test (Neo4J and JanusGraph are both tested in embedded mode)

The following commands clone the repository, build a JAR file and start GDBLancer:
```
git clone https://github.com/InverseIntegral/gdbmeter.git
cd gdblancer
mvn package -DskipTests
cd target
java -jar gdbmeter-*.jar
```
This will run GDBMeter until a bug has been found.

## Testing Approaches


| **Name**         | **Description**                                                                                                                                             |
|------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Partitioning     | This approach is based on Ternary Logic Partitioning (TLP). The core idea is that we can partition a result set based on a predicate into disjoint subsets. |
| Empty Result     | A query that has an empty result set should return an empty result whenever no new nodes are added.                                                         |
| Non-Empty Result | A query that has a non-empty result set should remain unchanged as long as we don't remove any nodes of said result set.                                    |
| Refinement       | Matching on (a subset of) the exact values of a previous query should return (a superset of) the previous result set.                                       |


## Supported GDBMS

| **GDBMS**                                              | **Status** | **Description**                                                                                                                           |
|--------------------------------------------------------|------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| [Neo4J](https://github.com/neo4j/neo4j)                | Working    | Reference implementation for other GDBMS. This implementation is based on the common Cypher logic.                                        |
| [RedisGraph](https://github.com/RedisGraph/RedisGraph) | Working    | This implementation is based on the common Cypher logic. Running this implementation will most likely uncover more unreported logic bugs. |
| [JanusGraph](https://github.com/JanusGraph/janusgraph) | Working    | Supports a (small) subset of the Gremlin query language. Currently, only the inmemory version with the lucene index backend are tested.   |

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
