@file:Suppress("UnstableApiUsage")

include(":firebase-domain")


rootProject.name = "FireMessenger"
include(":app")
include(":domain")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}