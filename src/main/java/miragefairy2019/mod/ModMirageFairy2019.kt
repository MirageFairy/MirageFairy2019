package miragefairy2019.mod

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

    private val initializer = InitializerMirageFairy2019()

    @Mod.EventHandler
    fun handle(event: FMLConstructionEvent) = initializer.construction(event)

    @Mod.EventHandler
    fun handle(event: FMLPreInitializationEvent) = initializer.preInit(event)

    @Mod.EventHandler
    fun handle(event: FMLInitializationEvent) = initializer.init(event)

    @Mod.EventHandler
    fun handle(event: FMLPostInitializationEvent) = initializer.postInit(event)

    @Mod.EventHandler
    fun handle(event: FMLLoadCompleteEvent) = initializer.loadComplete(event)

    @Mod.EventHandler
    fun handle(event: FMLServerStartingEvent) = initializer.serverStarting(event)

    @Mod.EventHandler
    fun handle(event: FMLServerStartedEvent) = initializer.serverStarted(event)

    @Mod.EventHandler
    fun handle(event: FMLServerStoppingEvent) = initializer.serverStopping(event)

    @Mod.EventHandler
    fun handle(event: FMLServerStoppedEvent) = initializer.serverStopped(event)
}
