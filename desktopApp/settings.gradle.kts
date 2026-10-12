pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.jvm") version "2.2.10"
        id("org.jetbrains.compose") version "1.9.0"
        id("org.jetbrains.kotlin.plugin.compose") version "2.2.10"
    }
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "PCP-Solver-Desktop"
