plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    maven("https://repo.spongepowered.org/repository/maven-public/") {
        content {
            includeGroup("org.spongepowered")
            includeGroup("net.minecraftforge")
        }
    }
}

dependencies {
    implementation("org.spongepowered:vanillagradle:0.2.1-SNAPSHOT")
}