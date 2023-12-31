plugins {
    id("backend-convention")
}

repositories {
    mavenCentral()
}

dependencies {
    api(projects.core.types)
    api(projects.core.endpoints)

    api(projects.feature.chat.endpoints)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.websockets)
}
