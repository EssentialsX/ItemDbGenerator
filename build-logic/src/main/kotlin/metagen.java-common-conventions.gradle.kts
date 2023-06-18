plugins {
    java
}

group = rootProject.group "net.essentialsx.tools.metagen"
version = rootProject.version "3.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/groups/public/") {
        content { includeGroup("org.spigotmc") }
    }
    maven("https://repo.spongepowered.org/repository/maven-public/") {
        content {
            includeGroup("org.spongepowered")
            includeGroup("net.minecraftforge")
        }
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.9.3")
        }
    }
}
