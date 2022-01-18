package miragefairy2019.mod.lib.multi

import miragefairy2019.libkt.createItemStack
import net.minecraft.item.Item

open class ItemVariant {
    var metadata = 0
    var item: Item? = null

    @JvmOverloads // TODO remove
    fun createItemStack(amount: Int = 1) = item!!.createItemStack(amount, metadata)
}

open class ItemVariantMaterial(val registryName: String, val unlocalizedName: String) : ItemVariant()
