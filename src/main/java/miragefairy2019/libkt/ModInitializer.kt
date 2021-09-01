package miragefairy2019.libkt

import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

typealias Module = ModInitializer.() -> Unit

class ModInitializer {
    val onInstantiation = EventRegistry0()
    val onInitCreativeTab = EventRegistry0()
    val onPreInit = EventRegistry1<FMLPreInitializationEvent>()
    val onRegisterBlock = EventRegistry0()
    val onRegisterItem = EventRegistry0()
    val onCreateItemStack = EventRegistry0()
    val onInit = EventRegistry1<FMLInitializationEvent>()
    val onAddRecipe = EventRegistry0()
    val onRegisterItemColorHandler = EventRegistry0()
    val onPostInit = EventRegistry1<FMLPostInitializationEvent>()
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
