plugins {
    java
    `java-library`
    application
}

group = "ch.ethz.ast"
version = "1.0.0-SNAPSHOT"
description = "GDBMeter"
java.sourceCompatibility = JavaVersion.VERSION_17

dependencies {
    api("com.beust:jcommander:1.82")
    api("org.neo4j:neo4j:5.1.0")
    api("org.apache.maven.plugins:maven-compiler-plugin:3.10.1")
    api("ch.qos.logback:logback-classic:1.4.4")
    api("redis.clients:jedis:4.3.1")
    api("org.janusgraph:janusgraph-core:0.6.2")
    api("org.janusgraph:janusgraph-inmemory:0.6.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("ch.ethz.ast.gdbmeter.Main")
}

tasks.withType<Jar> {
    manifest { attributes(mapOf("Main-Class" to application.mainClass)) }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

    // Exclude other META-INFs
    exclude("META-INF/*.RSA")
    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
}

