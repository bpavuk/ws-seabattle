plugins {
    id("backend-convention")
}

repositories {
    mavenCentral()
}

dependencies {
    api(projects.core.types)

    implementation(libs.ktor.server.websockets)
}
