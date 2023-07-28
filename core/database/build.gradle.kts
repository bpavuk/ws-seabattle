plugins {
    id("backend-convention")
}

dependencies {
    // exposed deps via gradle version catalog
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.h2)
}
