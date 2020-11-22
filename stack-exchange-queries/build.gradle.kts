
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
        archiveBaseName.set("shadow")
        mergeServiceFiles()
        isZip64 = true // needed for hive-jdbc, do not enable if not needed
        manifest {
            attributes(mapOf("Main-Class" to "example.LoadInHIVEKt"))
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}


/*val fatJar = task("fatJar", type = Jar::class) {
    baseName = "${project.name}-fat"
    archiveClassifier.set("all")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    // manifest Main-Class attribute is optional.
    // (Used only to provide default main class for executable jar)
    manifest {
        attributes["Main-Class"] = "example.LoadInHIVEKt" // fully qualified class name of default main class
    }
    //from(configurations.runtime.map({ if (it.isDirectory) it else zipTree(it) }))
    from(configurations.runtimeClasspath.get()
        .onEach { println("add from dependencies: ${it.name}") }
        .map { if (it.isDirectory) it else zipTree(it) })
    with(tasks["jar"] as CopySpec)
    /*val sourcesMain = sourceSets.main.get()
    sourcesMain.allSource.forEach { println("add from sources: ${it.name}") }
    from(sourcesMain.output)*/
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}*/





