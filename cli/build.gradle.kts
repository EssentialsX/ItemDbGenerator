plugins {
    id("metagen.java-application-conventions")
    id("metagen.vanillagradle-conventions")
}

dependencies {
    implementation(project(":metagen-common"))
    implementation(project(":metagen-source-vanilla"))

    implementation("cloud.commandframework:cloud-services:1.8.3")
}

application {
    // Define the main class for the application.
    mainClass.set("net.essentialsx.tools.metagen.cli.Main")
}
