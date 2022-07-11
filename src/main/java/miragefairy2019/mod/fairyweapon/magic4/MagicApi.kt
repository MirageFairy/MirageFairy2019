package miragefairy2019.mod.fairyweapon.magic4

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand

interface MagicArguments : FormulaArguments {
    val weaponItem: Item
    val weaponItemStack: ItemStack
    val player: EntityPlayer
    operator fun <T> MagicStatus<T>.invoke() = formula.calculate(this@MagicArguments)
}

open class MagicHandler {
    open fun onItemRightClick(hand: EnumHand): EnumActionResult = EnumActionResult.PASS
    open fun onUsingTick(count: Int) = Unit
    open fun onUpdate(itemSlot: Int, isSelected: Boolean) = Unit
    open fun onClientUpdate(itemSlot: Int, isSelected: Boolean) = Unit
    open fun onServerUpdate(itemSlot: Int, isSelected: Boolean) = Unit
    open fun hitEntity(target: EntityLivingBase) = Unit // TODO 撃破時と分離
}

typealias Magic = MagicArguments.() -> MagicHandler
