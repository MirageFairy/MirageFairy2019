package miragefairy2019.mod3.artifacts.fairycrystal

import net.minecraft.item.ItemStack

interface IDrop {
    fun getItemStack(rank: Int): ItemStack
    val dropCategory: DropCategory
    val weight: Double
}
