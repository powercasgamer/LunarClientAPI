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

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.4.0")
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

rootProject.name = "LunarClientParent"

sequenceOf(
    "api",
    "nethandler"
).forEach {
    include("lunarclient-$it")
    project(":lunarclient-$it").projectDir = file(it)
}
