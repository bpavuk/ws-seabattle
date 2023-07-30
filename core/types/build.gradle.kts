plugins {
    id("backend-convention")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.websockets)
}
