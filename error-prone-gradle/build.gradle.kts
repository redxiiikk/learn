import net.ltgt.gradle.errorprone.CheckSeverity
import net.ltgt.gradle.errorprone.errorprone

plugins {
    id("java")
    id("net.ltgt.errorprone") version "3.1.0"
}

group = "com.github.redxiiikk.learn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.errorprone:error_prone_annotations:2.25.0")
    errorprone("com.google.errorprone:error_prone_core:2.25.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.errorprone {
        error("UnusedMethod")
        disableWarningsInGeneratedCode = true
    }
}
