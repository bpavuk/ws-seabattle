plugins {
    id("backend-convention")
}

repositories {
    mavenCentral()
}

dependencies {
    api(projects.feature.battle.usecase)
    api(projects.core.endpoints)

    api(libs.kotlinx.coroutines.core)
    implementation(libs.ktor.server.websockets)
}
