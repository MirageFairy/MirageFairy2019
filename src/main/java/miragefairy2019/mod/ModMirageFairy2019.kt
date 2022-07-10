package miragefairy2019.mod

import miragefairy2019.lib.modinitializer.ModScope
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


    private var modScope = ModScope(MODID, System.getProperty("miragefairy2019.usePreReleaseFeatures")?.toBoolean() ?: true)

    init {
        modules(modScope)
    }


    init {
        modScope.onInstantiation()
        modScope.onInitCreativeTab()
    }

    @Mod.EventHandler
    fun handle(event: FMLConstructionEvent) {
        modScope.onConstruction(event)
    }

    @Mod.EventHandler
    fun handle(event: FMLPreInitializationEvent) {
        modScope.onPreInit(event)
        modScope.onRegisterFluid()
        modScope.onRegisterBlock()
        modScope.onRegisterItem()
        modScope.onCreateItemStack()
        modScope.onHookDecorator()
        modScope.onInitKeyBinding()
    }

    @Mod.EventHandler
    fun handle(event: FMLInitializationEvent) {
        modScope.onInit(event)
        modScope.onAddRecipe()
        if (event.side.isClient) modScope.onRegisterItemColorHandler()
        modScope.onRegisterTileEntity()
        modScope.onRegisterTileEntityRenderer()
        modScope.onInitNetworkChannel()
        modScope.onRegisterNetworkMessage()
    }

    @Mod.EventHandler
    fun handle(event: FMLPostInitializationEvent) = modScope.onPostInit(event)

    @Mod.EventHandler
    fun handle(event: FMLLoadCompleteEvent) = modScope.onLoadComplete(event)

    @Mod.EventHandler
    fun handle(event: FMLServerStartingEvent) = modScope.onServerStarting(event)

    @Mod.EventHandler
    fun handle(event: FMLServerStartedEvent) = modScope.onServerStarted(event)

    @Mod.EventHandler
    fun handle(event: FMLServerStoppingEvent) = modScope.onServerStopping(event)

    @Mod.EventHandler
    fun handle(event: FMLServerStoppedEvent) = modScope.onServerStopped(event)
}
