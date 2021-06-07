plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    // Checker
    implementation("org.checkerframework:checker-qual:2.5.5")

    // JUnit
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.test {
    // Use junit platform for unit tests.
    useJUnitPlatform()
}
