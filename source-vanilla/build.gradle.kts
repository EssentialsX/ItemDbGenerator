plugins {
    id("metagen.java-library-conventions")
    id("metagen.vanillagradle-conventions")
}

dependencies {
    api(project(":metagen-common"))
}
