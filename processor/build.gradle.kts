plugins {
    id("net.essentialsx.aliasgen.java-library-conventions")
}

dependencies {
    api(project(":common"))
    implementation(libs.rorledning)
}
