plugins {
    id("backend-convention")
}

repositories {
    mavenCentral()
}

dependencies {
    api(projects.feature.battle.types)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.websockets)
}
