rootProject.name = "MirageFairy2019"
pluginManagement {
    repositories {
        mavenCentral()
        // for plugin: net.minecraftforge.gradle.forge
        maven("https://files.minecraftforge.net/maven")
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.jvm" -> useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72")
                "net.minecraftforge.gradle.forge" -> useModule("net.minecraftforge.gradle:ForgeGradle:2.3-SNAPSHOT")
            }
        }
    }
}
