import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer

plugins {
    java
    `java-library`
    application

    id("com.github.johnrengelman.shadow") version ("8.1.1")
}

group = "ch.ethz.ast"
version = "1.0.0-SNAPSHOT"
description = "GDBMeter"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    api("com.beust:jcommander:1.82")
    api("org.neo4j:neo4j:5.1.0")
    api("ch.qos.logback:logback-classic:1.4.4")
    api("redis.clients:jedis:4.3.1")
    api("org.janusgraph:janusgraph-core:0.6.2")
    api("org.janusgraph:janusgraph-inmemory:0.6.2")
    api("org.janusgraph:janusgraph-lucene:0.6.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("ch.ethz.ast.gdbmeter.Main")
}

tasks.withType<ShadowJar> {
    mergeServiceFiles()
    manifest { attributes(mapOf("Main-Class" to application.mainClass)) }
    transform(Log4j2PluginsCacheFileTransformer::class.java)
    isZip64 = true
}

tasks.test {
    useJUnitPlatform()
}
