package miragefairy2019.lib.modinitializer

import miragefairy2019.common.ResourceName
import miragefairy2019.libkt.ItemMulti
import miragefairy2019.libkt.ItemVariant
import miragefairy2019.mod.ModMirageFairy2019
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

class ItemVariantInitializer<out I : ItemMulti<V>, V : ItemVariant>(val itemInitializer: ItemInitializer<I>, override val resourceName: ResourceName, val metadata: Int, getter: () -> V) : Initializer<V>(getter), NamedInitializer {
    override val modInitializer get() = itemInitializer.modInitializer
    val itemVariant get() = initializingObject
}

fun <I : ItemMulti<V>, V : ItemVariant> ItemInitializer<I>.itemVariant(
    registryName: String,
    creator: (String) -> V,
    metadata: Int,
    initializer: (ItemVariantInitializer<I, V>.() -> Unit)? = null
): ItemVariantInitializer<I, V> {
    lateinit var itemVariant: V
    modInitializer.onRegisterItem {
        itemVariant = creator(registryName)
        item.registerVariant(metadata, itemVariant)
    }
    return ItemVariantInitializer(this, ResourceName(ModMirageFairy2019.MODID, registryName), metadata) { itemVariant }.also {
        if (initializer != null) it.initializer()
    }
}

fun <I : ItemMulti<V>, V : ItemVariant> ItemVariantInitializer<I, V>.addOreName(oreName: String) = itemInitializer.modInitializer.onCreateItemStack { itemVariant.addOreName(oreName) }
fun <V : ItemVariant> V.addOreName(oreName: String) = OreDictionary.registerOre(oreName, ItemStack(item, 1, metadata))
fun <I : ItemMulti<V>, V : ItemVariant> ItemVariantInitializer<I, V>.createItemStack(amount: Int = 1): ItemStack = itemVariant.createItemStack(amount)
