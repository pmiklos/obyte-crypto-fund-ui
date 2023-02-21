import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    distribution
}

group "app.obyte.cryptofund"
version "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    js(IR) {
        browser {
            testTask {
                testLogging.showStandardStreams = true
                useKarma {
                    useChromeHeadless()
                    useFirefox()
                }
            }
        }
        binaries.executable()
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)
                implementation(compose.runtime)
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

val jsBrowserDistribution by tasks.getting

distributions {
    create("githubPages") {
        distributionBaseName.set("github-pages")
        contents {
            from (jsBrowserDistribution)
            exclude {
                it.name.startsWith("github-pages")
            }
        }
    }
}

val githubPagesDistTar by tasks.getting(Tar::class) {
    dependsOn(jsBrowserDistribution)
    compression = Compression.GZIP
    archiveExtension.set("tar.gz")
}
