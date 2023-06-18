import org.spongepowered.gradle.vanilla.repository.MinecraftPlatform

plugins {
    id("metagen.java-common-conventions")
    id("org.spongepowered.gradle.vanilla")
}

minecraft {
    version("1.20.1")
    platform(MinecraftPlatform.SERVER)
}
