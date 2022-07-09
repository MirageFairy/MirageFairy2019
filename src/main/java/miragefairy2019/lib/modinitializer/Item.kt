package miragefairy2019.lib.modinitializer

import miragefairy2019.common.ResourceName
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.oredict.OreDictionary

class ItemInitializer<out I : Item>(override val modInitializer: ModInitializer, override val resourceName: ResourceName, getter: () -> I) : Initializer<I>(getter), NamedInitializer {
    val item get() = initializingObject
}

fun <I : Item> ModInitializer.item(creator: () -> I, registryName: String, initializer: (ItemInitializer<I>.() -> Unit)? = null): ItemInitializer<I> {
    lateinit var item: I
    onRegisterItem {
        item = creator()
        item.setRegistryName(ModMirageFairy2019.MODID, registryName)
        ForgeRegistries.ITEMS.register(item)
    }
    return ItemInitializer(this, ResourceName(modId, registryName)) { item }.also {
        if (initializer != null) it.initializer()
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
    if (Main.side.isClient) {
        ModelLoader.setCustomModelResourceLocation(this, metadata, ModelResourceLocation(model ?: registryName!!, variant))
    }
}


fun <I : Item> ItemInitializer<I>.addOreName(oreName: String, metadata: Int = 0) = modInitializer.onCreateItemStack { item.addOreName(oreName, metadata) }
fun <I : Item> I.addOreName(oreName: String, metadata: Int = 0) = OreDictionary.registerOre(oreName, ItemStack(this, 1, metadata))
