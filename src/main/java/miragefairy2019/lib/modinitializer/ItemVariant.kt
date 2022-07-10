package miragefairy2019.lib.modinitializer

import miragefairy2019.common.ResourceName
import miragefairy2019.libkt.ItemMulti
import miragefairy2019.libkt.ItemVariant
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

class ItemVariantScope<out I : ItemMulti<V>, V : ItemVariant>(val itemScope: ItemScope<I>, override val resourceName: ResourceName, val metadata: Int, getter: () -> V) : ObjectScope<V>(getter), NamedScope {
    override val modScope get() = itemScope.modScope
    val itemVariant get() = initializingObject
}

fun <I : ItemMulti<V>, V : ItemVariant> ItemScope<I>.itemVariant(
    registryName: String,
    creator: (String) -> V,
    metadata: Int,
    initializer: (ItemVariantScope<I, V>.() -> Unit)? = null
): ItemVariantScope<I, V> {
    lateinit var itemVariant: V
    modScope.onRegisterItem {
        itemVariant = creator(registryName)
        item.registerVariant(metadata, itemVariant)
    }
    return ItemVariantScope(this, ResourceName(modScope.modId, registryName), metadata) { itemVariant }.also {
        if (initializer != null) it.initializer()
    }
}

fun <I : ItemMulti<V>, V : ItemVariant> ItemVariantScope<I, V>.addOreName(oreName: String) = itemScope.modScope.onCreateItemStack {
    OreDictionary.registerOre(oreName, ItemStack(itemVariant.item, 1, metadata))
}

fun <I : ItemMulti<V>, V : ItemVariant> ItemVariantScope<I, V>.createItemStack(amount: Int = 1): ItemStack = itemVariant.createItemStack(amount)
