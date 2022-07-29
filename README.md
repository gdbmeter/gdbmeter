# GDBLancer

[![Test](https://github.com/InverseIntegral/gdblancer/actions/workflows/maven.yml/badge.svg)](https://github.com/InverseIntegral/gdblancer/actions/workflows/maven.yml)

GDBLancer is a tool that automatically tests Graph Database Management Systems (GDBMS).
It tries to find logic bugs i.e. bugs that cause the GDBMS to fetch an incorrect result set.

## Getting Started

Required Software:
- Java 11
- Maven
- The graph database you want to test (Neo4J and JanusGraph are both tested as embedded software)

The following commands clone the repository, build a JAR file and start GDBLancer:
```
git clone git@github.com:InverseIntegral/gdblancer.git
cd gdblancer
mvn package -DskipTests
cd target
java -jar gdblancer-*.jar
```
This will run GDBLancer until a bug has been found.

## Testing Approaches

## Supported GDBMS

| **GDBMS**                                              | **Status** | **Description**                                                                                                                           |
|--------------------------------------------------------|------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| [Neo4J](https://github.com/neo4j/neo4j)                | Working    | Reference implementation for other GDBMS. This implementation is based on the common Cypher logic.                                        |
| [RedisGraph](https://github.com/RedisGraph/RedisGraph) | Working    | This implementation is based on the common Cypher logic. Running this implementation will most likely uncover more unreported logic bugs. |
| [JanusGraph](https://github.com/JanusGraph/janusgraph) | Working    | Supports a (small) subset of the Gremlin query language. Currently, only the inmemory version with the lucene index backend are tested.   |

## Found bugs

## Using GDBLancer

### Logs

### Reproducing a Bug

### Reducing a Bug

### Found Bugs
