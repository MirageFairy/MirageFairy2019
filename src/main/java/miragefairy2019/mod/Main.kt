package miragefairy2019.mod

import miragefairy2019.libkt.GuiHandlerContext
import miragefairy2019.libkt.ISimpleGuiHandler
import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.guiHandler
import miragefairy2019.libkt.module
import miragefairy2019.mod.artifacts.FairyCrystal
import mirrg.kotlin.log4j.hydrogen.getLogger
import net.minecraft.creativetab.CreativeTabs
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.common.network.IGuiHandler
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.logging.log4j.Logger

object Main {
    lateinit var logger: Logger
    lateinit var side: Side
    lateinit var creativeTab: CreativeTabs

    lateinit var simpleNetworkWrapper: SimpleNetworkWrapper

    private val guiHandlers = mutableMapOf<Int, IGuiHandler>()
    fun registerGuiHandler(id: Int, guiHandler: IGuiHandler) {
        if (id in guiHandlers) throw RuntimeException("Duplicated GuiId: $id")
        guiHandlers[id] = guiHandler
    }

    const val categoryGeneral = "general"
    const val categoryFeatures = "features"

    val mainModule = module {
        onConstruction {
            getLogger().info("Mod Version: $modVersion; Use Pre-Release Features: $usePreReleaseFeatures;")
        }

        onPreInit {
            logger = modLog
            Main.side = side
        }

        onInitCreativeTab {
            creativeTab = object : CreativeTabs("mirageFairy2019") {
                @SideOnly(Side.CLIENT)
                override fun getTabIconItem() = FairyCrystal.variantFairyCrystal().createItemStack()
            }
        }

        onInitNetworkChannel {
            simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(ModMirageFairy2019.MODID)
        }

        // Gui Handler
        onInit {
            NetworkRegistry.INSTANCE.registerGuiHandler(ModMirageFairy2019.instance, object : ISimpleGuiHandler {
                override fun GuiHandlerContext.onServer() = guiHandlers[id]?.getServerGuiElement(id, player, world, x, y, z)
                override fun GuiHandlerContext.onClient() = guiHandlers[id]?.getClientGuiElement(id, player, world, x, y, z)
            }.guiHandler)
        }

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
