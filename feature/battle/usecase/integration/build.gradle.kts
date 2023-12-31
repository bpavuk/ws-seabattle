plugins {
    id("backend-convention")
}

repositories {
    mavenCentral()
}

dependencies {
    api(projects.feature.battle.usecase)
    api(projects.feature.battle.endpoints)
}
