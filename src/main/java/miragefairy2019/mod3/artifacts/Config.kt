package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.module
import net.minecraftforge.common.config.Configuration

object Config {
    val categoryGeneral = "general"
    val categoryFeatures = "features"
    val module = module {
        onPreInit {
            val configuration = Configuration(suggestedConfigurationFile)
            configProperties.forEach { it.configure(configuration) }
            configuration.save()
        }
    }
}

private val configProperties = mutableListOf<ConfigPropertyHandler<*>>()

private class ConfigPropertyHandler<T : Any>(private val propertySelector: (Configuration) -> T) : () -> T {
    lateinit var value: T
    fun configure(configuration: Configuration) {
        value = propertySelector(configuration)
    }

    override fun invoke() = value
}

/** プロパティは[ModInitializer.onPreInit]よりも後で利用できます。 */
fun <T : Any> configProperty(propertySelector: (Configuration) -> T): () -> T = ConfigPropertyHandler(propertySelector).also { configProperties += it }
