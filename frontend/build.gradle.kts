plugins {
    id("backend-convention")
}

repositories {
    mavenCentral()
}

dependencies {
    api(projects.core.endpoints)
    api(projects.core.types)

    api(projects.feature.battle.endpoints)
    api(projects.feature.chat.endpoints)

    implementation(libs.ktor.server.websockets)
}
