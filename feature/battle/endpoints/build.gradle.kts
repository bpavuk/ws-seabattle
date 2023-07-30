plugins {
    id("backend-convention")
}

repositories {
    mavenCentral()
}

dependencies {
    api(projects.core.types)
    api(projects.core.endpoints)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.websockets)
}
