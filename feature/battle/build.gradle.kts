plugins {
    kotlin("jvm") version libs.versions.kotlin.version
}

repositories {
    mavenCentral()
}

dependencies {
    api(projects.feature.battle.endpoints)
}

kotlin {
    jvmToolchain(8)
}