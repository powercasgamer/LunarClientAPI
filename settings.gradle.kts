pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.stellardrift.ca/repository/snapshots/") {
            mavenContent {
                snapshotsOnly()
            }
        }
        maven("https://repo.deltapvp.net/")
        mavenLocal()
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "LunarClientParent"
sequenceOf(
    "api",
    "nethandler",
).forEach {
    include("lunarclient-$it")
    project(":lunarclient-$it").projectDir = file(it)
}
