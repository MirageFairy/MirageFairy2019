package miragefairy2019.mod

import miragefairy2019.libkt.ModInitializer
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLConstructionEvent
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartedEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent

@Mod(
    modid = ModMirageFairy2019.MODID,
    name = ModMirageFairy2019.NAME,
    version = ModMirageFairy2019.VERSION,
    acceptableRemoteVersions = ModMirageFairy2019.ACCEPTABLE_REMOTE_VERSIONS
)
class ModMirageFairy2019 {
    companion object {
        const val MODID = "miragefairy2019"
        const val NAME = "MirageFairy2019"
        const val VERSION = "{version}"
        const val ACCEPTABLE_REMOTE_VERSIONS = "{acceptableRemoteVersions}"

        @JvmStatic
        @Mod.Instance
        var instance: ModMirageFairy2019? = null
    }


    private var modInitializer = ModInitializer(System.getProperty("miragefairy2019.usePreReleaseFeatures")?.toBoolean() ?: true)

    init {
        modules(modInitializer)
    }


    init {
        modInitializer.onInstantiation()
        modInitializer.onInitCreativeTab()
    }

    @Mod.EventHandler
    fun handle(event: FMLConstructionEvent) {
        modInitializer.onConstruction(event)
    }

    @Mod.EventHandler
    fun handle(event: FMLPreInitializationEvent) {
        modInitializer.onPreInit(event)
        modInitializer.onRegisterFluid()
        modInitializer.onRegisterBlock()
        modInitializer.onRegisterItem()
        modInitializer.onCreateItemStack()
        modInitializer.onHookDecorator()
        modInitializer.onInitKeyBinding()
    }

    @Mod.EventHandler
    fun handle(event: FMLInitializationEvent) {
        modInitializer.onInit(event)
        modInitializer.onAddRecipe()
        if (event.side.isClient) modInitializer.onRegisterItemColorHandler()
        modInitializer.onRegisterTileEntity()
        modInitializer.onRegisterTileEntityRenderer()
        modInitializer.onInitNetworkChannel()
        modInitializer.onRegisterNetworkMessage()
    }

    @Mod.EventHandler
    fun handle(event: FMLPostInitializationEvent) = modInitializer.onPostInit(event)

    @Mod.EventHandler
    fun handle(event: FMLLoadCompleteEvent) = modInitializer.onLoadComplete(event)

    @Mod.EventHandler
    fun handle(event: FMLServerStartingEvent) = modInitializer.onServerStarting(event)

    @Mod.EventHandler
    fun handle(event: FMLServerStartedEvent) = modInitializer.onServerStarted(event)

    @Mod.EventHandler
    fun handle(event: FMLServerStoppingEvent) = modInitializer.onServerStopping(event)

    @Mod.EventHandler
    fun handle(event: FMLServerStoppedEvent) = modInitializer.onServerStopped(event)
}
