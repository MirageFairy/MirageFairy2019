package miragefairy2019.mod3.fertilizer

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.item
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemDye
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

val moduleFertilizer: Module = {

    // 肥料
    item({ ItemFertilizer() }, "fertilizer") {
        setUnlocalizedName("fertilizer")
        setCreativeTab { ApiMain.creativeTab }
        setCustomModelResourceLocation()
    }

}

private class ItemFertilizer : Item() {
    override fun onItemUse(player: EntityPlayer, world: World, blockPos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        val itemStack = player.getHeldItem(hand)

        if (!player.canPlayerEdit(blockPos.offset(facing), facing, itemStack)) return EnumActionResult.FAIL

        if (ItemDye.applyBonemeal(itemStack, world, blockPos, player, hand)) {
            if (!world.isRemote) world.playEvent(2005, blockPos, 0)
            return EnumActionResult.SUCCESS
        }

        return EnumActionResult.PASS
    }
}
