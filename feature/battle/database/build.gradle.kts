plugins {
    id("backend-convention")
}

repositories {
    mavenCentral()
}

dependencies {
    api(projects.feature.battle.types)

    api(libs.exposed.core)
    api(libs.exposed.jdbc)
}
