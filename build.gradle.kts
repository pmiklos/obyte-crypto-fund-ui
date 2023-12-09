plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
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
                implementation(compose.html.core)
                implementation(compose.runtime)
                implementation(npm("obyte", "0.1.23"))
                implementation(devNpm("stream-browserify", "3.0.0"))
                implementation(devNpm("buffer", "6.0.3"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

