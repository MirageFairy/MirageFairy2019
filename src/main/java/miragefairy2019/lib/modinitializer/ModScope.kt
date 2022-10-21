package miragefairy2019.lib.modinitializer

import miragefairy2019.lib.resourcemaker.LangMaker
import miragefairy2019.lib.resourcemaker.ResourceMaker
import miragefairy2019.mod.ModMirageFairy2019
import mirrg.kotlin.log4j.hydrogen.getLogger
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.event.FMLConstructionEvent
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartedEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

fun module(block: ModScope.() -> Unit) = block

class ModScope(val modId: String, val usePreReleaseFeatures: Boolean) {
    val modVersion = run {
        val version = ModMirageFairy2019.VERSION
        val serverVersion = version.split(".").getOrNull(2)?.toInt()
        getLogger().info("Version: $version; Server Version: $serverVersion;")
        serverVersion
    }

    fun checkModVersion(sinceVersion: Int) = usePreReleaseFeatures || (modVersion != null && modVersion >= sinceVersion)

    val onMakeResource = EventRegistry1<ResourceMaker>()
    val onMakeLang = EventRegistry1<LangMaker>()
    val onMakeIngredientFactory = EventRegistry1<MutableMap<String, Class<*>>>()

    val onConstruction = EventRegistry1<FMLConstructionEvent>()
    val onInstantiation = EventRegistry0()
    val onInitCreativeTab = EventRegistry0()

    val onPreInit = EventRegistry1<FMLPreInitializationEvent>()
    val onRegisterFluid = EventRegistry0()
    val onRegisterBlock = EventRegistry0()
    val onRegisterItem = EventRegistry0()
    val onCreateItemStack = EventRegistry0()
    val onHookDecorator = EventRegistry0()
    val onInitKeyBinding = EventRegistry0()

    val onInit = EventRegistry1<FMLInitializationEvent>()
    val onAddRecipe = EventRegistry0()
    val onRegisterItemColorHandler = EventRegistry0()
    val onRegisterTileEntity = EventRegistry0()
    val onRegisterTileEntityRenderer = EventRegistry0()
    val onInitNetworkChannel = EventRegistry0()
    val onRegisterNetworkMessage = EventRegistry0()

    val onPostInit = EventRegistry1<FMLPostInitializationEvent>()

    val onLoadComplete = EventRegistry1<FMLLoadCompleteEvent>()
    val onServerStarting = EventRegistry1<FMLServerStartingEvent>()
    val onServerStarted = EventRegistry1<FMLServerStartedEvent>()
    val onServerStopping = EventRegistry1<FMLServerStoppingEvent>()
    val onServerStopped = EventRegistry1<FMLServerStoppedEvent>()
}

class EventRegistry0 {
    private val list = mutableListOf<() -> Unit>()
    operator fun invoke(listener: () -> Unit) = run { list += listener }
    operator fun invoke() = list.forEach { it() }
}

class EventRegistry1<E> {
    private val list = mutableListOf<E.() -> Unit>()
    operator fun invoke(listener: E.() -> Unit) = run { list += listener }
    operator fun invoke(event: E) = list.forEach { it(event) }
}


fun ModScope.onServerSave(listener: WorldEvent.Save.() -> Unit) = onInit {
    MinecraftForge.EVENT_BUS.register(object {
        @[Suppress("unused") SubscribeEvent]
        fun handle(event: WorldEvent.Save) {
            if (event.world.provider.dimension == 0) event.listener()
        }
    })
}
