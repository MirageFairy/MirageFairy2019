package miragefairy2019.mod3.artifacts.fairycrystal

import miragefairy2019.libkt.blue
import miragefairy2019.libkt.drop
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.gold
import miragefairy2019.libkt.orNull
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.totalWeight
import miragefairy2019.mod.lib.ItemVariant
import miragefairy2019.mod3.skill.EnumMastery
import miragefairy2019.mod3.skill.api.ApiSkill
import miragefairy2019.mod3.skill.api.ISkillContainer
import miragefairy2019.mod3.skill.displayName
import miragefairy2019.mod3.skill.getSkillLevel
import mirrg.kotlin.formatAs
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.stats.StatList
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World

open class VariantFairyCrystal(val registryName: String, val unlocalizedName: String, val oreName: String) : ItemVariant() {
    fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        val mastery = EnumMastery.fairySummoning
        val skillContainer = ApiSkill.skillManager.clientSkillContainer
        tooltip += formattedText { (!"スキル: " + !mastery.displayName + !" (${skillContainer.getSkillLevel(mastery)})").gold } // TODO translate
        tooltip += formattedText { (!"レア判定ブースト: ${getRareBoost(skillContainer) * 100.0 formatAs "%.2f%%"}").blue } // TODO translate
    }

    fun getRareBoost(skillContainer: ISkillContainer): Double {
        val a = itemRareBoost
        val b = 1.0 + skillContainer.getSkillLevel(EnumMastery.fairySummoning) * 0.01
        return a * b
    }

    open val dropRank get() = 0
    open val itemRareBoost get() = 1.0
    val dropper
        get() = object : FairyCrystalDropper() {
            override val dropList get() = ApiFairyCrystal.dropsFairyCrystal
        }

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

class VariantFairyCrystalPure(registryName: String, unlocalizedName: String, oreName: String) : VariantFairyCrystal(registryName, unlocalizedName, oreName) {
    override val dropRank get() = 1
    override val itemRareBoost get() = 2.0
}
