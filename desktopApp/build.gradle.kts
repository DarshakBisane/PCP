plugins {
    kotlin("jvm") version "2.2.10"
    id("org.jetbrains.compose") version "1.9.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.10"
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
}

compose.desktop {
    application {
        mainClass = "com.example.desktop.MainKt"
        nativeDistributions {
            targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Exe)
            packageName = "PCP Solver"
            packageVersion = "1.0.0"
            description = "Interactive Post Correspondence Problem Solver"
            vendor = "Darshak K. Bisane"
        }
    }
}
