package miragefairy2019.mod

import net.minecraft.item.ItemStack

interface IFuelItem {
    fun getItemBurnTime(itemStack: ItemStack): Int
}
