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
include("feature:battle:usecase")
include("feature:battle:types")
include("feature:battle:usecase:integration")
include("core:endpoints")
include("feature:battle:database")
include("feature:battle:database:integration")
include("core:types")
findProject(":core:types")?.name = "types"
