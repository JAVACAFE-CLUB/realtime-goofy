import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    extra.apply {
    }
}

plugins {
    val kotlinVersion = "1.9.25"

    id("org.springframework.boot") version "3.5.4" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false

    kotlin("jvm") version kotlinVersion apply false
    kotlin("plugin.spring") version kotlinVersion apply false
    kotlin("plugin.jpa") version kotlinVersion apply false
    kotlin("plugin.allopen") version kotlinVersion apply false
    kotlin("kapt") version kotlinVersion apply false

    idea
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("org.jetbrains.kotlin.kapt")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("java")
    }

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    dependencies {
        /** Use the configurations from the Java plugin */
        "implementation"("org.jetbrains.kotlin:kotlin-reflect")
        "implementation"("com.fasterxml.jackson.module:jackson-module-kotlin")

        /** test */
        "testImplementation"("org.springframework.boot:spring-boot-starter-test")
        "testImplementation"("io.mockk:mockk:${DependencyVersion.MOCKK}")
        "testImplementation"("org.jetbrains.kotlinx:kotlinx-coroutines-test")
        "testImplementation"("io.kotest:kotest-runner-junit5:${DependencyVersion.KOTEST}")
        "testImplementation"("io.kotest:kotest-assertions-core:${DependencyVersion.KOTEST}")
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "8.7"
}
