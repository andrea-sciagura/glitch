/*
 * Copyright 2024-2026 Andrea Sciagura
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.PublishingExtension
import org.gradle.plugins.signing.SigningExtension

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("maven-publish")
    id("signing")
}

group = "xyz.andrea-sciagura.anim"
version = "1.0.0"

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Glitch"
            isStatic = true
        }
    }

    jvm()

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(compose.ui)
        }
    }
}

android {
    namespace = "xyz.andrea_sciagura.glitch"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

configure<PublishingExtension> {
    repositories {
        maven {
            name = "OSSRH"
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = (project.findProperty("mavenCentralUsername") ?: System.getenv("MAVEN_CENTRAL_USERNAME"))?.toString() ?: ""
                password = (project.findProperty("mavenCentralPassword") ?: System.getenv("MAVEN_CENTRAL_PASSWORD"))?.toString() ?: ""
            }
        }
    }
    publications.withType<MavenPublication> {
        val javadocJar = tasks.register("${name}JavadocJar", Jar::class) {
            archiveClassifier.set("javadoc")
            archiveAppendix.set(name)
        }
        artifact(javadocJar)

        pom {
            name.set("Glitch Animation Library")
            description.set("A Compose Multiplatform library for glitch animations.")
            url.set("https://github.com/andrea-sciagura/glitch")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("andrea-sciagura")
                    name.set("Andrea Sciagura")
                    email.set("email@andrea-sciagura.xyz")
                }
            }
            scm {
                connection.set("scm:git:git://github.com/andrea-sciagura/glitch.git")
                developerConnection.set("scm:git:ssh://github.com/andrea-sciagura/glitch.git")
                url.set("https://github.com/andrea-sciagura/glitch")
            }
        }
    }
}

configure<SigningExtension> {
    val keyId = (project.findProperty("signing.keyId")?.toString()?.takeIf { it.isNotBlank() } ?: System.getenv("SIGNING_KEY_ID"))
    val key = (project.findProperty("signing.key")?.toString()?.takeIf { it.isNotBlank() } ?: System.getenv("SIGNING_KEY"))
    val password = (project.findProperty("signing.password")?.toString()?.takeIf { it.isNotBlank() } ?: System.getenv("SIGNING_PASSWORD"))
    if (!keyId.isNullOrBlank() && !key.isNullOrBlank() && !password.isNullOrBlank()) {
        useInMemoryPgpKeys(keyId, key, password)
        sign(publishing.publications)
    }
}
