package miragefairy2019.mod.lib.multi

import miragefairy2019.libkt.createItemStack
import mirrg.boron.util.struct.Tuple
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import java.util.Optional

open class ItemMulti<V : ItemVariant> : Item() {
    init {
        setHasSubtypes(true)
        maxDamage = 0
    }

    //

    private val map = mutableMapOf<Int, V>()

    fun registerVariant(metadata: Int, variant: V) {
        require(metadata !in map) { "Illegal metadata: $metadata" }
        map[metadata] = variant
        variant.item = this
        variant.metadata = metadata
    }

    // TODO Nullable
    fun getVariant(itemStack: ItemStack) = getVariant(itemStack.metadata)

    // TODO Nullable
    fun getVariant(metadata: Int) = Optional.ofNullable(map[metadata])

    // TODO type
    val variants: Iterable<Tuple<Int, V?>> get() = map.entries.map { Tuple.of(it.key, it.value) }

    //

    override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>) {
        if (!isInCreativeTab(tab)) return
        map.keys.forEach { items += createItemStack(1, it) }
    }
}

open class ItemVariant {
    var metadata = 0
    var item: Item? = null

    @JvmOverloads // TODO remove
    fun createItemStack(amount: Int = 1) = item!!.createItemStack(amount, metadata)
}

open class ItemVariantMaterial(val registryName: String, val unlocalizedName: String) : ItemVariant()
