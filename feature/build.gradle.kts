plugins {
    id("backend-convention")
}

repositories {
    mavenCentral()
}

dependencies {
    api(projects.feature.battle)
    api(projects.feature.chat)
}
