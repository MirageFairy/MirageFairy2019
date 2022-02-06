package miragefairy2019.mod.modules.fairycrystal

import miragefairy2019.libkt.translateToLocal
import miragefairy2019.libkt.translateToLocalFormatted
import miragefairy2019.mod.lib.multi.ItemMulti
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ItemFairyCrystal : ItemMulti<VariantFairyCrystal>() {
    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        val itemStackCrystal = player.getHeldItem(hand)
        if (itemStackCrystal.isEmpty) return EnumActionResult.PASS
        val variant = getVariant(itemStackCrystal) ?: return EnumActionResult.PASS
        return variant.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ)
    }

    override fun getItemStackDisplayName(itemStack: ItemStack): String {
        val variant = getVariant(itemStack) ?: return translateToLocal("$unlocalizedName.name")
        return translateToLocalFormatted("item.${variant.unlocalizedName}.name")
    }

    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        val variant = getVariant(itemStack) ?: return
        variant.addInformation(itemStack, world, tooltip, flag)
    }
}
