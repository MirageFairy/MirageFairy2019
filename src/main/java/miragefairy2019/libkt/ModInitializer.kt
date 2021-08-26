package miragefairy2019.libkt

import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

class ModInitializer {
    val onInstantiation = EventRegistry<Unit>()
    val onPreInit = EventRegistry<FMLPreInitializationEvent>()
    val onInit = EventRegistry<FMLInitializationEvent>()
    val onPostInit = EventRegistry<FMLPostInitializationEvent>()
}

class EventRegistry<E> {
    private val list = mutableListOf<E.() -> Unit>()
    operator fun invoke(block: E.() -> Unit) = run { list += block }
    fun fire(event: E) = list.forEach { it(event) }
}
