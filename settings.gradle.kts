pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            // Plugins
            version("kotlin", "2.1.21")
            plugin("multiplatform", "org.jetbrains.kotlin.multiplatform").versionRef("kotlin")
            plugin("serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef("kotlin")
            plugin("kover", "org.jetbrains.kotlinx.kover").version("0.8.3")
            plugin("dokka", "org.jetbrains.dokka").version("2.0.0")
            plugin("ksp", "com.google.devtools.ksp").version("2.1.21-2.0.2")
            plugin("maven", "com.vanniktech.maven.publish").version("0.30.0")

            // Main
            library("kdriver", "dev.kdriver:core:0.2.3")
            library("mcp", "io.modelcontextprotocol:kotlin-sdk:0.5.0")
            library("logback-core", "ch.qos.logback:logback-core:1.5.18")
            library("logback-classic", "ch.qos.logback:logback-classic:1.5.18")

            // Tests
            library("tests-mockk", "io.mockk:mockk:1.13.12")
            library("tests-jsoup", "org.jsoup:jsoup:1.16.2")
            library("tests-coroutines", "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
        }
    }
}

rootProject.name = "kdriver-mcp"
include(":mcp")
