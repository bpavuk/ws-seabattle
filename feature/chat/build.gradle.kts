plugins {
    id("backend-convention")
}

repositories {
    mavenCentral()
}

dependencies {
    api(projects.feature.chat.endpoints)
    api(projects.feature.chat.usecase)
    api(projects.feature.chat.usecase.integration)
}
