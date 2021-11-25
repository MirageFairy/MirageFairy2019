package miragefairy2019.libkt

import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod3.main.api.ApiMain.side
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.oredict.OreDictionary
import java.util.function.Supplier

typealias Module = ModInitializer.() -> Unit

class ModInitializer {
    val onInstantiation = EventRegistry0()
    val onInitCreativeTab = EventRegistry0()

    val onPreInit = EventRegistry1<FMLPreInitializationEvent>()
    val onRegisterBlock = EventRegistry0()
    val onRegisterItem = EventRegistry0()
    val onCreateItemStack = EventRegistry0()
    val onHookDecorator = EventRegistry0()
    val onInitKeyBinding = EventRegistry0()

    val onInit = EventRegistry1<FMLInitializationEvent>()
    val onAddRecipe = EventRegistry0()
    val onRegisterItemColorHandler = EventRegistry0()
    val onRegisterTileEntity = EventRegistry0()
    val onInitNetworkChannel = EventRegistry0()
    val onRegisterNetworkMessage = EventRegistry0()

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


class ItemInitializer<T : Item>(val modInitializer: ModInitializer, private val sItem: () -> T) {
    val item get() = sItem()
}

fun <T : Item> ModInitializer.item(creator: () -> T, registryName: String, block: ItemInitializer<T>.() -> Unit): Supplier<T> {
    lateinit var item: T
    onRegisterItem {
        item = creator()
        item.setRegistryName(ModMirageFairy2019.MODID, registryName)
        ForgeRegistries.ITEMS.register(item)
    }
    ItemInitializer<T>(this) { item }.block()
    return Supplier { item }
}

fun <T : Item> ItemInitializer<T>.setUnlocalizedName(unlocalizedName: String) = modInitializer.onRegisterItem { item.unlocalizedName = unlocalizedName }
fun <T : Item> ItemInitializer<T>.setCreativeTab(creativeTab: () -> CreativeTabs) = modInitializer.onRegisterItem { item.creativeTab = creativeTab() }
fun <T : Item> ItemInitializer<T>.setCustomModelResourceLocation(metadata: Int = 0) = modInitializer.onRegisterItem { item.setCustomModelResourceLocation(metadata) }
fun <T : Item> ItemInitializer<T>.addOreName(oreName: String, metadata: Int = 0) = modInitializer.onCreateItemStack { OreDictionary.registerOre(oreName, ItemStack(item, 1, metadata)) }
fun <T : Item> T.setCustomModelResourceLocation(modelName: String, metadata: Int = 0) {
    if (side.isClient) {
        ModelLoader.setCustomModelResourceLocation(this, metadata, ModelResourceLocation(ResourceLocation(ModMirageFairy2019.MODID, modelName), "normal"))
    }
}

fun <T : Item> T.setCustomModelResourceLocation(metadata: Int = 0) {
    if (side.isClient) {
        ModelLoader.setCustomModelResourceLocation(this, metadata, ModelResourceLocation(registryName!!, "normal"))
    }
}
