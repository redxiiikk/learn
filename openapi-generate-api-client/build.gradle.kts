import org.jetbrains.kotlin.gradle.model.SourceSet

plugins {
    kotlin("jvm") version "1.9.21"
    id("org.openapi.generator") version "7.2.0"
}

group = "com.github.redxiiikk.learn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

openApiGenerate {
    generatorName.set("kotlin")

    inputSpec.set("${layout.projectDirectory}/src/main/resources/hello-world.yaml")
    outputDir.set("${layout.buildDirectory.asFile.get().path}/generated")

    packageName.set("com.github.redxiiikk.learn.client")
    apiPackage.set("com.github.redxiiikk.learn.client.api")
    modelPackage.set("com.github.redxiiikk.learn.client.model")
}

sourceSets {
    main {
        kotlin {
            srcDir("${layout.buildDirectory.asFile.get().path}/generated/src")
        }
    }
}
