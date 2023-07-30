plugins {
    id("backend-convention")
}

repositories {
    mavenCentral()
}

dependencies {
    api(projects.core.types)

    api(projects.feature.battle.database)
    api(projects.feature.battle.usecase)
}
