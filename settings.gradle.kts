@file:Suppress("UnstableApiUsage")

rootProject.name = "FireMessenger"
include(":domain")
include(":android-core")
include(":firebase-android-app")

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