import gg.meza.stonecraft.mod

plugins {
    id("gg.meza.stonecraft")
}

val awFile =
    when {
        stonecutter.current.parsed <= "1.20.4" -> project.rootProject.layout.projectDirectory.file("src/main/resources/accesswideners/1.20.accesswidener")
        stonecutter.current.parsed <= "1.21.4" -> project.rootProject.layout.projectDirectory.file("src/main/resources/accesswideners/1.21.accesswidener")
        else -> project.rootProject.layout.projectDirectory.file("src/main/resources/accesswideners/26.1.accesswidener")
    }

modSettings {
    accessWidenerLocation = awFile

    clientOptions {
        fov = 90
        guiScale = 3
        narrator = false
        musicVolume = 0.0
    }

    variableReplacements = mapOf(
        "arch" to mod.prop("arch", "*"),
        "anim" to mod.prop("animation", "*"),
        "midnight" to mod.prop("midnight", "*")
    )
}

repositories {
    maven("https://maven.kosmx.dev/")
    maven("https://maven.fzzyhmstrs.me/")
    maven("https://maven.architectury.dev/")
    maven("https://maven.terraformersmc.com/")
    maven("https://maven.midnightdust.eu/releases")
}

val isDeobfuscated = stonecutter.current.parsed >= "26.1"
dependencies {
    val imp = if (isDeobfuscated) "implementation" else "modImplementation"
    val api = if (isDeobfuscated) "api" else "modApi"
    val loader = when {
        mod.isForge -> "forge"
        mod.isNeoforge -> "neoforge"
        else -> "fabric"
    }

    add(imp, "dev.architectury:architectury-${loader}:${mod.prop("arch")}")

    if (stonecutter.current.parsed <= "1.20.4") {
        val midnightlib = "eu.midnightdust:midnightlib:${mod.prop("midnight")}+1.20.1-$loader"
        add(imp, midnightlib)
        include(midnightlib)
    }

    if (mod.isFabric) {
        add(imp, "me.fzzyhmstrs:fzzy_config:0.6.0+1.20.1")
    }

    if (stonecutter.current.parsed <= "1.20.4") add(imp, "dev.kosmx.player-anim:player-animation-lib-${loader}:${mod.prop("animation")}")
}

loom {
    if (mod.isForge) {
        forge {
            mixinConfigs("${mod.id}.mixins.json",)
        }
    }
}

stonecutter {
    replacements {
        string {
            direction = eval(current.version, ">=1.21.11")
            replace("ResourceLocation", "Identifier")
        }
        string {
            direction = eval(current.version, ">=1.21.11")
            replace("import net.minecraft.Util;", "import net.minecraft.util.Util;")
        }
        string {
            direction = eval(current.version, ">=1.21.11")
            replace("location", "identifier")
        }
    }
}

java {
    withSourcesJar()

    val javaVersion = when {
        stonecutter.eval(stonecutter.current.version, "<=1.20.4") -> 17
        stonecutter.eval(stonecutter.current.version, "<=1.21.4") -> 21
        else -> 25
    }

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }

    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)
}

if (mod.isForge) { // FORGE FIX for Gradle
    tasks.withType<JavaExec>().configureEach {
        dependsOn("generatePackMCMetaJson")
    }
}

publishMods {
//    modrinth {
//        if (mod.isFabric) requires("fabric-api")
//    }
//
//    curseforge {
//        client = true
//        server = true
//        if (mod.isFabric) requires("fabric-api")
//    }
}
