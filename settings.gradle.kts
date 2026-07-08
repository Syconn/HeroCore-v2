pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.kikugie.dev/releases")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev")
        maven("https://maven.minecraftforge.net")
        maven("https://maven.neoforged.net/releases/")
    }
}
plugins {
    id("gg.meza.stonecraft") version "1.12+"
    id("dev.kikugie.stonecutter") version "0.9+"
}

stonecutter {
    centralScript = "build.gradle.kts"
    kotlinController = true
    shared {
        fun mc(version: String, vararg loaders: String) {
            for (it in loaders) version("$version-$it", version)
        }

        mc("26.1", "fabric", "neoforge")
        mc("1.21.1", "fabric", "neoforge")
        mc("1.20.1", "fabric", "forge")

        vcsVersion = "1.20.1-fabric"
    }
    create(rootProject)
}

rootProject.name = "Stonecraft"
