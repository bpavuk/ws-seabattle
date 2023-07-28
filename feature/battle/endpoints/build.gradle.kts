plugins {
    kotlin("jvm") version libs.versions.kotlin.version
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.websockets)
}

kotlin {
    jvmToolchain(8)
}
