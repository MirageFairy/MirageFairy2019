package miragefairy2019.mod.magic4.api

import miragefairy2019.mod.formula4.api.FormulaArguments
import miragefairy2019.mod.formula4.api.MagicStatus
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWeaponFormula4
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand

interface MagicArguments : FormulaArguments {
    val weaponItem: ItemFairyWeaponFormula4
    val weaponItemStack: ItemStack
    val player: EntityPlayer
    operator fun <T> MagicStatus<T>.invoke() = formula.calculate(this@MagicArguments)
}

open class MagicHandler {
    open fun onItemRightClick(hand: EnumHand): EnumActionResult = EnumActionResult.PASS
    open fun onClientUpdate(itemSlot: Int, isSelected: Boolean) = Unit
    open fun hitEntity(target: EntityLivingBase) = Unit
}

typealias Magic = MagicArguments.() -> MagicHandler