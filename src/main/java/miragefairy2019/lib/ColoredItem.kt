package miragefairy2019.lib

import miragefairy2019.lib.modinitializer.ItemScope
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

interface IColoredItem {
    @SideOnly(Side.CLIENT)
    fun colorMultiplier(itemStack: ItemStack, tintIndex: Int): Int
}

fun <I> ItemScope<I>.registerItemColorHandler() where  I : Item, I : IColoredItem {
    modScope.onRegisterItemColorHandler {
        @SideOnly(Side.CLIENT)
        class ItemColorImpl : IItemColor {
            override fun getColorFromItemstack(itemStack: ItemStack, tintIndex: Int) = item.colorMultiplier(itemStack, tintIndex)
        }
        Minecraft.getMinecraft().itemColors.registerItemColorHandler(ItemColorImpl(), item)
    }
}
