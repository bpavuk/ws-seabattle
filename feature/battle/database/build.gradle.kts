plugins {
    id("backend-convention")
}

repositories {
    mavenCentral()
}

dependencies {
    api(projects.core.types)

    api(libs.exposed.core)
    api(libs.exposed.jdbc)
}
