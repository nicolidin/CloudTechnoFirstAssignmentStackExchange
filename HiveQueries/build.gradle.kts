
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

// import org.gradle.jvm.tasks.Jar

plugins {
    java
    kotlin("jvm") version "1.4.10"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.apache.hive:hive-jdbc:latest.release")
    implementation("org.apache.hadoop:hadoop-client:latest.release")
    //implementation("org.apache.hive:hive-jdbc:${hive-jdbc.version}")
    implementation("log4j:log4j:latest.release")
    testCompile("junit", "junit", "4.12")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("hive-queries")
        mergeServiceFiles()
        isZip64 = true // needed for hive-jdbc, do not enable if not needed
        manifest {
            attributes(mapOf("Main-Class" to "example.HiveQueriesKt"))
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}





