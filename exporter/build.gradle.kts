plugins {
    id("net.essentialsx.aliasgen.java-library-conventions")
}

dependencies {
    implementation(project(":common"))
    implementation(libs.jackson.databind)
}
