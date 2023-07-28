plugins {
    kotlin("jvm") version libs.versions.kotlin.version
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(8)
}

dependencies {
    api(projects.feature.battle)
}
