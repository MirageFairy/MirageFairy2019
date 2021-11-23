package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.red
import miragefairy2019.mod.modules.fairycrystal.ItemFairyCrystal
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemStack
import net.minecraft.stats.StatList
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class ItemFairyWandSummoning : ItemFairyWeaponCraftingTool() {
    @SideOnly(Side.CLIENT)
    override fun addInformationFunctions(itemStack: ItemStack, world: World, tooltip: MutableList<String>, flag: ITooltipFlag) {
        tooltip.add(formattedText { (!"Hold right mouse button to use fairy crystals quickly").red }) // TODO translate
        super.addInformationFunctions(itemStack, world, tooltip, flag)
    }

    //

    override fun getItemUseAction(stack: ItemStack) = EnumAction.BOW
    override fun getMaxItemUseDuration(stack: ItemStack) = 72000 // 永続

    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        player.activeHand = hand
        return ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand))
    }

    override fun onUsingTick(stack: ItemStack, entityLivingBase: EntityLivingBase, count: Int) {
        if (entityLivingBase.world.isRemote) return

        if (entityLivingBase is EntityPlayer) {

            // 使用Tickじゃないなら抜ける
            if (!isUsingTick(count)) return

            // 妖晶を得る
            val itemStackFairyCrystal = findItem(entityLivingBase) { itemStack -> itemStack!!.item is ItemFairyCrystal }.orElse(null) ?: return
            val variantFairyCrystal = (itemStackFairyCrystal.item as ItemFairyCrystal).getVariant(itemStackFairyCrystal).orElse(null) ?: return

            // プレイヤー視点判定
            val rayTraceResult = rayTrace(entityLivingBase.world, entityLivingBase, false) ?: return // ブロックに当たらなかった場合は無視
            if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) return // ブロックに当たらなかった場合は無視

            // ガチャを引く
            val itemStackDrop = variantFairyCrystal.dropper.drop(
                entityLivingBase,
                entityLivingBase.world,
                rayTraceResult.blockPos,
                if (entityLivingBase.getHeldItem(EnumHand.MAIN_HAND).item === this) EnumHand.MAIN_HAND else EnumHand.OFF_HAND,
                rayTraceResult.sideHit,
                rayTraceResult.hitVec.x.toFloat(),
                rayTraceResult.hitVec.y.toFloat(),
                rayTraceResult.hitVec.z.toFloat()
            ).orElse(null) ?: return // ガチャが引けなかった場合は無視
            if (itemStackDrop.isEmpty) return // ガチャが引けなかった場合は無視

            // 成立

            // ガチャアイテムを消費
            if (!entityLivingBase.isCreative) itemStackFairyCrystal.shrink(1)
            entityLivingBase.addStat(StatList.getObjectUseStats(itemStackFairyCrystal.item))

            // 妖精をドロップ
            val blockPos = rayTraceResult.blockPos.offset(rayTraceResult.sideHit)
            val entityItem = EntityItem(entityLivingBase.world, blockPos.x + 0.5, blockPos.y + 0.5, blockPos.z + 0.5, itemStackDrop.copy())
            entityItem.setNoPickupDelay()
            entityLivingBase.world.spawnEntity(entityItem)

        }
    }

    private fun isUsingTick(count: Int): Boolean {
        val t = 72000 - count
        return when {
            t >= 60 -> true
            t >= 20 -> t % 2 == 0
            t >= 5 -> t % 5 == 0
            t == 1 -> true
            else -> false
        }
    }
}
