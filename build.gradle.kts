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

version = "1.0"
group = "com.yourname.modid"
base.archivesBaseName = "modid"

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
    // !!gitHubAction-minecraftVersion="1.12.2-14.23.5.2847"
    runDir = "run"
    mappings = "snapshot_20171003"
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.72")
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
