import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecraftforge.gradle.user.TaskSourceCopy
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File
import java.net.URL

buildscript {
    dependencies {
        // for bug: ClassNotFoundException: kotlin.coroutines.Continuation
        classpath(files("lib/kotlin-stdlib-1.3.72.jar"))
    }
}

plugins {
    application
    kotlin("jvm")
    id("net.minecraftforge.gradle.forge")
    id("com.github.johnrengelman.shadow")
    id("com.dorongold.task-tree") version "1.5"
}

// 各数字は上位のバージョンが増えてもリセットしない
// 上位のバージョン数値が上がるとき、その下の数値もすべて上がる
// 正式版でないので常に0
// Forge対応バージョンの区別
// クライアント互換性の区別
// ビルドバージョンの区別
val versionTag = "v0.1.27.64"
val versionZero = versionTag.substring(1).split(".")[0].toInt()
val versionForge = versionTag.substring(1).split(".")[1].toInt()
val versionCompatibility = versionTag.substring(1).split(".")[2].toInt()
val versionBuild = versionTag.substring(1).split(".")[3].toInt()

version = "${versionZero}.${versionForge}.${versionCompatibility}.${versionBuild}"
group = "miragefairy2019"
base.archivesBaseName = "MirageFairy2019"

tasks.withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
    options.encoding = "UTF-8"
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

minecraft {
    version = "1.12.2-14.23.5.2847"
    // !!gitHubAction-minecraftVersion="1.12.2"
    runDir = "run"
    mappings = "snapshot_20171003"
    clientRunArgs = listOf("--username", "Player1")
}

application.mainClassName = "dummy" // shadowJarの例外を抑制するため

lateinit var adder: Configuration
configurations {
    adder = create("adder")
}

repositories {
    mavenCentral()
    maven("https://mirrgieriana.github.io/mirrg.boron/maven")
    // Progwml6 maven: location of the maven that hosts JEI files
    maven("https://dvs1.progwml6.com/files/maven/")
    // ModMaven: location of a maven mirror for JEI files, as a fallback
    maven("https://modmaven.k-4u.nl")
}

dependencies {
    testCompile("junit:junit:4.12")

    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.72")
    adder("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.72")

    compile("mirrg.boron:mirrg.boron.util:[3.19,4[")
    adder("mirrg.boron:mirrg.boron.util:[3.19,4[")

    compile("mirrg.boron:mirrg.boron.swing:0.1+")

    compile("mezz.jei:jei_1.12.2:4.16.1.302")
    //runtime("mezz.jei:jei_1.12.2:4.16.1.302")

    compile(files("lib/appliedenergistics2/appliedenergistics2-rv6-stable-7-api.jar"))
}

tasks {

    register("fetchMirrgKotlin") {
        doFirst {
            fun fetch(fileName: String) {
                val file = File("src/main/java").resolve(fileName)
                when {
                    file.parentFile.isDirectory -> Unit
                    file.parentFile.exists() -> throw RuntimeException("Already exists: ${file.parentFile}")
                    !file.parentFile.mkdirs() -> throw RuntimeException("Could not create the directory: ${file.parentFile}")
                }
                file.writeBytes(URL("https://raw.githubusercontent.com/MirrgieRiana/mirrg.kotlin/main/src/main/java/$fileName").readBytes())
            }
            fetch("mirrg/kotlin/hydrogen/String.kt")
            fetch("mirrg/kotlin/hydrogen/Collection.kt")
            fetch("mirrg/kotlin/hydrogen/Lang.kt")
            fetch("mirrg/kotlin/hydrogen/Number.kt")
            fetch("mirrg/kotlin/java/hydrogen/File.kt")
            fetch("mirrg/kotlin/java/hydrogen/Optional.kt")
            fetch("mirrg/kotlin/log4j/hydrogen/Logging.kt")
            fetch("mirrg/kotlin/gson/hydrogen/Gson.kt")
            fetch("mirrg/kotlin/gson/hydrogen/JsonWrapper.kt")
        }
    }

    named<TaskSourceCopy>("sourceMainKotlin") {
        include("ModMirageFairy2019.kt")
        replace("{version}", project.version)
        replace("{acceptableRemoteVersions}", "[${versionZero}.${versionForge}.${versionCompatibility}.0,${versionZero}.${versionForge}.${versionCompatibility + 1}.0)")
    }

    named<ProcessResources>("processResources") {
        inputs.property("version", project.version)
        inputs.property("mcversion", project.minecraft.version)
        from(sourceSets["main"].resources.srcDirs) {
            include("mcmod.info")
            expand(
                mapOf(
                    "version" to project.version,
                    "mcversion" to project.minecraft.version
                )
            )
        }
        from(sourceSets["main"].resources.srcDirs) {
            exclude("mcmod.info")
        }
    }

    register<JavaExec>("makeResources") {
        main = "miragefairy2019.mod.MainMakeResource"
        classpath = (tasks["runClient"] as JavaExec).classpath
        dependsOn("assemble")
    }

    named<Jar>("jar") {
        finalizedBy("reobfJar")
        classifier = "original"
        version = "${project.minecraft.version}-${project.version}"
    }

    named<ShadowJar>("shadowJar") {
        finalizedBy("reobfShadowJar")
        classifier = ""
        version = "${project.minecraft.version}-${project.version}"
        configurations = listOf(adder)
        listOf(
            "kotlin",
            "org.intellij.lang.annotations",
            "org.jetbrains.annotations",
            "mirrg.boron",
            "mirrg.kotlin"
        ).forEach {
            relocate(it, "${project.group}.$it")
        }
    }

}

reobf {
    create("shadowJar")
}

artifacts {
    add("archives", tasks["shadowJar"])
}
