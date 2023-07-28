@file:Suppress("UnstableApiUsage")

rootProject.name = "ws-seabattle"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}

includeBuild("build-logic")


include("application")
include("core")
include("core:database")
include("feature")
include("feature:battle")
include("feature:battle:endpoints")
