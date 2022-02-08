package miragefairy2019.mod.modules.fairycrystal

import miragefairy2019.libkt.drop
import miragefairy2019.libkt.orNull
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.totalWeight
import miragefairy2019.mod3.skill.api.ApiSkill
import mirrg.kotlin.formatAs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.stats.StatList
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World

open class VariantFairyCrystal(registryName: String, unlocalizedName: String, oreName: String) : VariantFairyCrystalBase(registryName, unlocalizedName, oreName) {
    fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        val crystalItemStack = player.getHeldItem(hand).orNull ?: return EnumActionResult.PASS // アイテムが消えた場合は中止
        if (world.isRemote) return EnumActionResult.SUCCESS // サーバースレッドのみ

        val rareBoost = getRareBoost(ApiSkill.skillManager.getServerSkillContainer(player))

        if (!player.isSneaking) {

            // ガチャを引く
            val resultItemStack = dropper.drop(player, world, pos, hand, facing, hitX, hitY, hitZ, dropRank, rareBoost)?.orNull ?: return EnumActionResult.SUCCESS

            // ガチャ成立

            // ガチャアイテムを消費
            crystalItemStack.shrink(1)
            StatList.getObjectUseStats(crystalItemStack.item)?.let { player.addStat(it) }

            // 妖精をドロップ
            val pos2 = pos.offset(facing)
            resultItemStack.drop(world, Vec3d(pos2).addVector(0.5, 0.5, 0.5), noPickupDelay = true)

            return EnumActionResult.SUCCESS
        } else {

            // ガチャリスト取得
            val dropTable = dropper.getDropTable(player, world, pos, hand, facing, hitX, hitY, hitZ, dropRank, rareBoost)


            // 表示

            fun send(textComponent: ITextComponent) = player.sendStatusMessage(textComponent, false)

            send(textComponent { !"===== " + !crystalItemStack.displayName + !" (${if (world.isRemote) "Client" else "Server"}) =====" })

            val totalWeight = dropTable.totalWeight
            dropTable.sortedBy { it.item.displayName }.sortedBy { it.weight }.forEach { weightedItem ->
                send(textComponent { !"${weightedItem.weight / totalWeight * 100.0 formatAs "%f%%"}: " + !weightedItem.item.displayName })
            }

            send(textComponent { !"====================" })


            return EnumActionResult.SUCCESS
        }
    }
}
