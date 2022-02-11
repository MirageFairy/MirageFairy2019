package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.red
import miragefairy2019.mod.modules.fairycrystal.ItemFairyCrystal
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWeaponBase.findItemOptional
import miragefairy2019.mod3.skill.api.ApiSkill
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

class ItemFairyWandSummoning(val maxTryCountPerTick: Int) : ItemFairyWand() {
    @SideOnly(Side.CLIENT)
    override fun addInformationFeatures(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        tooltip.add(formattedText { (!"Hold right mouse button to use fairy crystals quickly").red }) // TODO translate
        super.addInformationFeatures(itemStack, world, tooltip, flag)
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
            repeat(getTryCount(count)) a@{
                if (!tryUseCrystal(entityLivingBase)) return@a
            }
        }
    }

    private fun tryUseCrystal(player: EntityPlayer): Boolean {

        // 妖晶を得る
        val itemStackFairyCrystal = findItemOptional(player) { itemStack -> itemStack!!.item is ItemFairyCrystal }.orElse(null) ?: return false // クリスタルを持ってない場合は無視
        val variantFairyCrystal = (itemStackFairyCrystal.item as ItemFairyCrystal).getVariant(itemStackFairyCrystal) ?: return false // 異常なクリスタルを持っている場合は無視

        // プレイヤー視点判定
        val rayTraceResult = rayTrace(player.world, player, false) ?: return false // ブロックに当たらなかった場合は無視
        if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) return false // ブロックに当たらなかった場合は無視

        // ガチャを引く
        val itemStackDrop = variantFairyCrystal.dropper.drop(
            player,
            player.world,
            rayTraceResult.blockPos,
            if (player.getHeldItem(EnumHand.MAIN_HAND).item === this) EnumHand.MAIN_HAND else EnumHand.OFF_HAND,
            rayTraceResult.sideHit,
            rayTraceResult.hitVec.x.toFloat(),
            rayTraceResult.hitVec.y.toFloat(),
            rayTraceResult.hitVec.z.toFloat(),
            variantFairyCrystal.dropRank,
            variantFairyCrystal.getRareBoost(ApiSkill.skillManager.getServerSkillContainer(player))
        ) ?: return false // ガチャが引けなかった場合は無視
        if (itemStackDrop.isEmpty) return false // ガチャが引けなかった場合は無視

        // 成立

        // ガチャアイテムを消費
        if (!player.isCreative) itemStackFairyCrystal.shrink(1)
        player.addStat(StatList.getObjectUseStats(itemStackFairyCrystal.item))

        // 妖精をドロップ
        val blockPos = rayTraceResult.blockPos.offset(rayTraceResult.sideHit)
        val entityItem = EntityItem(player.world, blockPos.x + 0.5, blockPos.y + 0.5, blockPos.z + 0.5, itemStackDrop.copy())
        entityItem.setNoPickupDelay()
        player.world.spawnEntity(entityItem)

        return true
    }

    private fun getTryCount(count: Int): Int {
        val t = 72000 - count
        return when {
            t >= 200 -> 5
            t >= 100 -> 2
            t >= 60 -> 1
            t >= 20 -> if (t % 2 == 0) 1 else 0
            t >= 5 -> if (t % 5 == 0) 1 else 0
            t == 1 -> 1
            else -> 0
        }.coerceAtMost(maxTryCountPerTick)
    }
}
