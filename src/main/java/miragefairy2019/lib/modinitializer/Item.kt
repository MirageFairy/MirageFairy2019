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

class ItemScope<out I : Item>(override val modScope: ModScope, override val resourceName: ResourceName, getter: () -> I) : ObjectScope<I>(getter), NamedScope {
    val item get() = initializingObject
}

fun <I : Item> ModScope.item(creator: () -> I, registryName: String, initializer: (ItemScope<I>.() -> Unit)? = null): ItemScope<I> {
    lateinit var item: I
    onRegisterItem {
        item = creator()
        item.setRegistryName(ModMirageFairy2019.MODID, registryName)
        ForgeRegistries.ITEMS.register(item)
    }
    return ItemScope(this, ResourceName(modId, registryName)) { item }.also {
        if (initializer != null) it.initializer()
    }
}


fun <I : Item> ItemScope<I>.setUnlocalizedName(unlocalizedName: String) = modScope.onRegisterItem { item.unlocalizedName = unlocalizedName }
fun <I : Item> ItemScope<I>.setCreativeTab(creativeTab: () -> CreativeTabs) = modScope.onRegisterItem { item.creativeTab = creativeTab() }

fun <I : Item> ItemScope<I>.setCustomModelResourceLocation(metadata: Int = 0, model: ResourceLocation? = null, variant: String = "normal") = modScope.onRegisterItem {
    if (Main.side.isClient) {
        ModelLoader.setCustomModelResourceLocation(item, metadata, ModelResourceLocation(model ?: item.registryName!!, variant))
    }
}

fun <I : Item> ItemScope<I>.addOreName(oreName: String, metadata: Int = 0) = modScope.onCreateItemStack {
    OreDictionary.registerOre(oreName, ItemStack(item, 1, metadata))
}
