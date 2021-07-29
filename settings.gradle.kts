rootProject.name = "MirageFairy2019"
pluginManagement {
    repositories {
        mavenCentral()
        // for plugin: net.minecraftforge.gradle.forge
        maven("https://files.minecraftforge.net/maven")
        // for plugin: com.github.johnrengelman.shadow
        maven("https://plugins.gradle.org/m2/")
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.jvm" -> useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72")
                "net.minecraftforge.gradle.forge" -> useModule("net.minecraftforge.gradle:ForgeGradle:2.3-SNAPSHOT")
                "com.github.johnrengelman.shadow" -> useModule("com.github.jengelman.gradle.plugins:shadow:4.0.4")
            }
        }
    }
}
