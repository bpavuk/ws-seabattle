plugins {
    id("backend-convention")
}

repositories {
    mavenCentral()
}

dependencies {
    api(projects.feature.battle)
}
