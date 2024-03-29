package miragefairy2019.mod

import miragefairy2019.lib.modinitializer.ModScope
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.resourcemaker.lang
import miragefairy2019.libkt.GuiHandlerEvent
import miragefairy2019.libkt.ISimpleGuiHandler
import miragefairy2019.libkt.ISimpleGuiHandlerTileEntity
import miragefairy2019.libkt.guiHandler
import miragefairy2019.libkt.tileEntity
import miragefairy2019.mod.artifacts.variantFairyCrystal
import mirrg.kotlin.hydrogen.castOrNull
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

        lang("itemGroup.mirageFairy2019", "MirageFairy2019", "MirageFairy2019")
        onInitCreativeTab {
            creativeTab = object : CreativeTabs("mirageFairy2019") {
                @SideOnly(Side.CLIENT)
                override fun getTabIconItem() = variantFairyCrystal().createItemStack()
            }
        }

        onInitNetworkChannel {
            simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(ModMirageFairy2019.MODID)
        }

        // Gui Handler
        onInit {
            NetworkRegistry.INSTANCE.registerGuiHandler(ModMirageFairy2019.instance, object : ISimpleGuiHandler {
                override fun onServer(event: GuiHandlerEvent) = guiHandlers[event.id]?.getServerGuiElement(event.id, event.player, event.world, event.x, event.y, event.z)
                override fun onClient(event: GuiHandlerEvent) = guiHandlers[event.id]?.getClientGuiElement(event.id, event.player, event.world, event.x, event.y, event.z)
            }.guiHandler)
        }

        // TileEntity共有GuiId
        onInit {
            registerGuiHandler(GuiId.commonTileEntity, object : ISimpleGuiHandler {
                override fun onServer(event: GuiHandlerEvent) = event.tileEntity?.castOrNull<ISimpleGuiHandlerTileEntity>()?.onServer(event)
                override fun onClient(event: GuiHandlerEvent) = event.tileEntity?.castOrNull<ISimpleGuiHandlerTileEntity>()?.onClient(event)
            }.guiHandler)
        }

        onPreInit {
            val configuration = Configuration(suggestedConfigurationFile)
            configProperties.forEach { it.configure(configuration) }
            configuration.save()
        }

        // Common Translation
        lang("miragefairy2019.gui.duration.days", "days", "日")
        lang("miragefairy2019.gui.duration.hours", "hours", "時間")
        lang("miragefairy2019.gui.duration.minutes", "minutes", "分")
        lang("miragefairy2019.gui.duration.seconds", "seconds", "秒")
        lang("miragefairy2019.gui.duration.milliSeconds", "milli seconds", "ミリ秒")

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

/** プロパティは[ModScope.onPreInit]よりも後で利用できます。 */
fun <T : Any> configProperty(propertySelector: (Configuration) -> T): () -> T = ConfigPropertyHandler(propertySelector).also { configProperties += it }
