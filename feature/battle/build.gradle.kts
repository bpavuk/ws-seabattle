plugins {
    id("backend-convention")
}

repositories {
    mavenCentral()
}

dependencies {
    api(projects.feature.battle.database)
    api(projects.feature.battle.database.integration)
    api(projects.feature.battle.usecase)
    api(projects.feature.battle.usecase.integration)
    api(projects.feature.battle.endpoints)
    api(projects.feature.battle.types)
}
