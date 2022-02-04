package miragefairy2019.libkt

import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.lib.multi.ItemMulti
import miragefairy2019.mod.lib.multi.ItemVariant
import miragefairy2019.mod3.main.api.ApiMain.side
import net.minecraft.block.Block
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.event.FMLConstructionEvent
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartedEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary

typealias Module = ModInitializer.() -> Unit

class ModInitializer {
    val onMakeResource = EventRegistry1<ResourceMaker>()

    val onConstruction = EventRegistry1<FMLConstructionEvent>()
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


// ResourceName

data class ResourceName(val domain: String, val path: String) {
    override fun toString() = "$domain:$path"
}

val ResourceName.resourceLocation get() = ResourceLocation(domain, path)
val ResourceLocation.resourceName get() = ResourceName(resourceDomain, resourcePath)


// Initializer
abstract class Initializer<T : Any>(private val getter: () -> T) : () -> T {
    internal val initializingObject get() = getter()
    override operator fun invoke() = initializingObject
}


// Item

class ItemInitializer<I : Item>(val modInitializer: ModInitializer, val registryName: ResourceName, getter: () -> I) : Initializer<I>(getter) {
    val item get() = initializingObject
}

fun <I : Item> ModInitializer.item(creator: () -> I, registryName: String, block: (ItemInitializer<I>.() -> Unit)? = null): ItemInitializer<I> {
    lateinit var item: I
    onRegisterItem {
        item = creator()
        item.setRegistryName(ModMirageFairy2019.MODID, registryName)
        ForgeRegistries.ITEMS.register(item)
    }
    return ItemInitializer(this, ResourceName(ModMirageFairy2019.MODID, registryName)) { item }.also {
        if (block != null) it.block()
    }
}

fun <I : Item> ItemInitializer<I>.setUnlocalizedName(unlocalizedName: String) = modInitializer.onRegisterItem { item.unlocalizedName = unlocalizedName }
fun <I : Item> ItemInitializer<I>.setCreativeTab(creativeTab: () -> CreativeTabs) = modInitializer.onRegisterItem { item.creativeTab = creativeTab() }


fun <I : Item> ItemInitializer<I>.setCustomModelResourceLocation(
    metadata: Int = 0,
    @Suppress("UNUSED_PARAMETER") vararg vs: Void,
    model: ResourceLocation? = null,
    variant: String = "normal"
) = modInitializer.onRegisterItem {
    item.setCustomModelResourceLocation(metadata, model = model, variant = variant)
}

fun <I : Item> I.setCustomModelResourceLocation(
    metadata: Int = 0,
    @Suppress("UNUSED_PARAMETER") vararg vs: Void,
    model: ResourceLocation? = null,
    variant: String = "normal"
) {
    if (side.isClient) {
        ModelLoader.setCustomModelResourceLocation(this, metadata, ModelResourceLocation(model ?: registryName!!, variant))
    }
}


fun <I : Item> ItemInitializer<I>.addOreName(oreName: String, metadata: Int = 0) = modInitializer.onCreateItemStack { item.addOreName(oreName, metadata) }
fun <I : Item> I.addOreName(oreName: String, metadata: Int = 0) = OreDictionary.registerOre(oreName, ItemStack(this, 1, metadata))


// ItemVariant

class ItemVariantInitializer<I : ItemMulti<V>, V : ItemVariant>(val itemInitializer: ItemInitializer<I>, val registryName: ResourceName, getter: () -> V) : Initializer<V>(getter) {
    val itemVariant get() = initializingObject
}

fun <I : ItemMulti<V>, V : ItemVariant> ItemInitializer<I>.itemVariant(
    registryName: String,
    creator: (String) -> V,
    metadata: Int,
    block: (ItemVariantInitializer<I, V>.() -> Unit)? = null
): ItemVariantInitializer<I, V> {
    lateinit var itemVariant: V
    modInitializer.onRegisterItem {
        itemVariant = creator(registryName)
        item.registerVariant(metadata, itemVariant)
    }
    return ItemVariantInitializer(this, ResourceName(ModMirageFairy2019.MODID, registryName)) { itemVariant }.also {
        if (block != null) it.block()
    }
}

fun <I : ItemMulti<V>, V : ItemVariant> ItemVariantInitializer<I, V>.addOreName(oreName: String) = itemInitializer.modInitializer.onCreateItemStack { itemVariant.addOreName(oreName) }
fun <V : ItemVariant> V.addOreName(oreName: String) = OreDictionary.registerOre(oreName, ItemStack(item, 1, metadata))
fun <I : ItemMulti<V>, V : ItemVariant> ItemVariantInitializer<I, V>.createItemStack(amount: Int = 1): ItemStack = itemVariant.createItemStack(amount)


// Block

class BlockInitializer<B : Block>(val modInitializer: ModInitializer, val resourceName: ResourceName, getter: () -> B) : Initializer<B>(getter) {
    val block get() = initializingObject
}

fun <B : Block> ModInitializer.block(creator: () -> B, registryName: String, block: (BlockInitializer<B>.() -> Unit)? = null): BlockInitializer<B> {
    lateinit var block2: B
    onRegisterBlock {
        block2 = creator()
        block2.setRegistryName(ModMirageFairy2019.MODID, registryName)
        ForgeRegistries.BLOCKS.register(block2)
    }
    return BlockInitializer(this, ResourceName(ModMirageFairy2019.MODID, registryName)) { block2 }.also {
        if (block != null) it.block()
    }
}

fun <B : Block> BlockInitializer<B>.setUnlocalizedName(unlocalizedName: String) = modInitializer.onRegisterItem { block.unlocalizedName = unlocalizedName }
fun <B : Block> BlockInitializer<B>.setCreativeTab(creativeTab: () -> CreativeTabs) = modInitializer.onRegisterItem { block.setCreativeTab(creativeTab()) }


// misc

fun <T : TileEntity> ModInitializer.tileEntity(registerName: String, clazz: Class<T>) {
    onRegisterTileEntity {
        GameRegistry.registerTileEntity(clazz, ResourceName(ModMirageFairy2019.MODID, registerName).resourceLocation)
    }
}

fun <T : TileEntity, R : TileEntitySpecialRenderer<T>> ModInitializer.tileEntityRenderer(classTileEntity: Class<T>, creatorRenderer: () -> R) {
    onRegisterTileEntityRenderer {
        if (side.isClient) {
            object : Any() {
                @SideOnly(Side.CLIENT)
                fun run() {
                    ClientRegistry.bindTileEntitySpecialRenderer(classTileEntity, creatorRenderer())
                }
            }.run()
        }
    }
}
