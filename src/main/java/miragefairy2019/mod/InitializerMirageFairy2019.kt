package miragefairy2019.mod

import miragefairy2019.libkt.ModInitializer
import net.minecraftforge.fml.common.event.FMLConstructionEvent
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartedEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent

class InitializerMirageFairy2019 {
    var modInitializer = ModInitializer(System.getProperty("miragefairy2019.usePreReleaseFeatures")?.toBoolean() ?: true)

    init {
        modules.forEach { it(modInitializer) }

        modInitializer.onInstantiation()
        modInitializer.onInitCreativeTab()
    }

    fun construction(event: FMLConstructionEvent) {
        modInitializer.onConstruction(event)
    }

    fun preInit(event: FMLPreInitializationEvent) {
        modInitializer.onPreInit(event)
        modInitializer.onRegisterFluid()
        modInitializer.onRegisterBlock()
        modInitializer.onRegisterItem()
        modInitializer.onCreateItemStack()
        modInitializer.onHookDecorator()
        modInitializer.onInitKeyBinding()
    }

    fun init(event: FMLInitializationEvent) {
        modInitializer.onInit(event)
        modInitializer.onAddRecipe()
        if (event.side.isClient) modInitializer.onRegisterItemColorHandler()
        modInitializer.onRegisterTileEntity()
        modInitializer.onRegisterTileEntityRenderer()
        modInitializer.onInitNetworkChannel()
        modInitializer.onRegisterNetworkMessage()
    }

    fun postInit(event: FMLPostInitializationEvent) {
        modInitializer.onPostInit(event)
    }

    fun loadComplete(event: FMLLoadCompleteEvent) {
        modInitializer.onLoadComplete(event)
    }

    fun serverStarting(event: FMLServerStartingEvent) {
        modInitializer.onServerStarting(event)
    }

    fun serverStarted(event: FMLServerStartedEvent) {
        modInitializer.onServerStarted(event)
    }

    fun serverStopping(event: FMLServerStoppingEvent) {
        modInitializer.onServerStopping(event)
    }

    fun serverStopped(event: FMLServerStoppedEvent) {
        modInitializer.onServerStopped(event)
    }
}
