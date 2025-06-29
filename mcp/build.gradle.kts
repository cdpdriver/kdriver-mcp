@file:OptIn(ExperimentalWasmDsl::class, ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.serialization)
    alias(libs.plugins.kover)
    alias(libs.plugins.ksp)
    alias(libs.plugins.maven)
}

mavenPublishing {
    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
    pom {
        name.set("mcp")
        description.set("MCP server for kdriver.")
        url.set("https://github.com/guimauvedigital/kdriver-mcp")

        licenses {
            license {
                name.set("GPL-3.0")
                url.set("https://opensource.org/licenses/GPL-3.0")
            }
        }
        developers {
            developer {
                id.set("NathanFallet")
                name.set("Nathan Fallet")
                email.set("contact@nathanfallet.me")
                url.set("https://www.nathanfallet.me")
            }
        }
        scm {
            url.set("https://github.com/guimauvedigital/kdriver-mcp.git")
        }
    }
}

val jvmMainClass = "dev.kdriver.mcp.MainKt"

kotlin {
    // jvm
    jvmToolchain(21)
    jvm {
        testRuns.named("test") {
            executionTask.configure {
                useJUnitPlatform()
            }
        }
        binaries {
            executable {
                mainClass.set(jvmMainClass)
            }
        }
        val jvmJar by tasks.getting(org.gradle.jvm.tasks.Jar::class) {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            doFirst {
                manifest {
                    attributes["Main-Class"] = jvmMainClass
                }
                from(configurations["jvmRuntimeClasspath"].map { if (it.isDirectory) it else zipTree(it) })
            }
        }
    }

    applyDefaultHierarchyTemplate()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kdriver)
                api(libs.mcp)

                implementation(platform("io.ktor:ktor-bom:3.1.3")) // Needed since mcp uses an older version of Ktor with a bug

                implementation(libs.logback.core)
                implementation(libs.logback.classic)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.tests.mockk)
            }
        }
    }
}
