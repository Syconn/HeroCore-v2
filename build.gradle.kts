import gg.meza.stonecraft.mod

plugins {
    id("gg.meza.stonecraft")
}

val awFile =
    when {
        stonecutter.current.parsed >= "1.21" -> project.rootProject.layout.projectDirectory.file("src/main/resources/accesswideners/1.21.accesswidener")
        stonecutter.current.parsed >= "1.20" -> project.rootProject.layout.projectDirectory.file("src/main/resources/accesswideners/1.20.accesswidener")
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
}

repositories {
    maven("https://maven.kosmx.dev/")
    maven("https://maven.blamejared.com/")
    maven("https://maven.fzzyhmstrs.me/")
    maven("https://maven.terraformersmc.com/")
    maven("https://thedarkcolour.github.io/KotlinForForge/")
    maven("https://maven.architectury.dev/")
}

val isDeobfuscated = stonecutter.current.parsed >= "26.1"
dependencies {
    val imp = if (isDeobfuscated) "implementation" else "modImplementation"
    val api = if (isDeobfuscated) "api" else "modApi"

    if (mod.isFabric) {
        add(imp, "dev.architectury:architectury-fabric:${mod.prop("arch")}")
    } else if (mod.isForge) {
        add(imp, "dev.architectury:architectury-forge:${mod.prop("arch")}")
    } else {
        add(imp, "dev.architectury:architectury-neoforge:${mod.prop("arch")}")
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

    val javaCompact = when {
        stonecutter.eval(stonecutter.current.version, "<=1.20.4") -> JavaVersion.VERSION_17
        stonecutter.eval(stonecutter.current.version, "<=1.21.4") -> JavaVersion.VERSION_21
        else -> JavaVersion.VERSION_25
    }

    sourceCompatibility = javaCompact
    targetCompatibility = javaCompact
}

if (mod.isForge) { // FORGE FIX for Gradle
    tasks.withType<JavaExec>().configureEach {
        dependsOn("generatePackMCMetaJson")
    }
}

publishMods {
    modrinth {
        if (mod.isFabric) requires("fabric-api")
    }

    curseforge {
        client = true
        server = true
        if (mod.isFabric) requires("fabric-api")
    }
}
