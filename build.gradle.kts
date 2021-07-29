import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    }
}

plugins {
    application
    kotlin("jvm")
    id("net.minecraftforge.gradle.forge")
}

// 正式版でないので常に0
// Forge対応バージョンの区別
// クライアント互換性の区別
// ビルドバージョンの区別
// 各数字は上位のバージョンが増えてもリセットしない
version = "0.1.13.19"
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

minecraft {
    version = "1.12.2-14.23.5.2847"
    // !!gitHubAction-minecraftVersion="1.12.2-14.23.5.2847"
    runDir = "run"
    mappings = "snapshot_20171003"
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.72")

    compile("mirrg.boron:mirrg.boron.util:[3.19,4[")
    adder("mirrg.boron:mirrg.boron.util:[3.19,4[")

    compile("mirrg.boron:mirrg.boron.swing:0.1+")

    compile("mezz.jei:jei_1.12.2:4.16.1.302")
    //runtime("mezz.jei:jei_1.12.2:4.16.1.302")

    compile(files("lib/appliedenergistics2/appliedenergistics2-rv6-stable-7-api.jar"))
}

(tasks["processResources"] as ProcessResources).run {
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

task<Exec>("makeJson") {
    executable = "bash"
    args("make_json.sh")
}
