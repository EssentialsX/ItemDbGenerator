pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.spongepowered.org/repository/maven-public/") {
            content {
                includeGroup("org.spongepowered")
                includeGroup("net.minecraftforge")
            }
        }
    }

    includeBuild("build-logic")
}

rootProject.name = "metagen-root"

sequenceOf(
    "common",
    "source-vanilla",
    "cli"
).forEach {
    include("metagen-$it")
    project(":metagen-$it").projectDir = file("./$it")
}

include("ItemDbGenerator")
project(":ItemDbGenerator").projectDir = file("./gen-legacy")
