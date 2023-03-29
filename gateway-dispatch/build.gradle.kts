import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.github.redxiiikk.learn"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.8.10"
    id("org.springframework.boot") version "3.0.2"
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}


subprojects {
    apply {
        plugin("kotlin")
        plugin("org.springframework.boot")
    }

    repositories {
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        implementation(platform("org.springframework.cloud:spring-cloud-dependencies:2021.0.4"))
        implementation(platform("org.springframework.boot:spring-boot-dependencies:2.6.11"))
        implementation(platform("com.alibaba.cloud:spring-cloud-alibaba-dependencies:2021.0.4.0"))

        implementation("org.jetbrains.kotlin:kotlin-reflect")

        testImplementation(kotlin("test"))
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }

    tasks.test {
        useJUnitPlatform()
    }
}