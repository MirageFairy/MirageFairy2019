package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.lib.proxy
import miragefairy2019.lib.skillContainer
import miragefairy2019.libkt.BlockRegion
import miragefairy2019.libkt.getRandomItem
import miragefairy2019.libkt.orNull
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.artifacts.ItemFairyCrystal
import miragefairy2019.mod.artifacts.getRateBoost
import miragefairy2019.mod.fairyweapon.findItem
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.integer
import miragefairy2019.mod.fairyweapon.magic4.magic
import miragefairy2019.mod.fairyweapon.magic4.map
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.systems.DropCategory
import miragefairy2019.mod.systems.FairyCrystalDropEnvironment
import miragefairy2019.mod.systems.getDropTable
import miragefairy2019.mod.systems.insertBiome
import miragefairy2019.mod.systems.insertBlocks
import miragefairy2019.mod.systems.insertEntities
import miragefairy2019.mod.systems.insertItemStacks
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemStack
import net.minecraft.stats.StatList
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.math.RayTraceResult
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class ItemPrayerWheel(private val maxTryCountPerTick: Int) : ItemFairyWeaponMagic4() {
    val chargeSpeed = status("maxSpeed", { maxTryCountPerTick }, { integer.map { textComponent { "${value * 20} Hz"() } } })

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = "右クリック長押しでフェアリークリスタルを高速消費" // TODO translate Hold right mouse button to use fairy crystals quickly


    override fun getItemUseAction(stack: ItemStack) = EnumAction.BOW
    override fun getMaxItemUseDuration(stack: ItemStack) = 72000 // 永続

    override fun getMagic() = magic {
        object : MagicHandler() {
            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                player.activeHand = hand
                return EnumActionResult.SUCCESS
            }

            override fun onUsingTick(count: Int) {
                if (player.world.isRemote) return
                useCrystal(player, getTryCount(count) atMost chargeSpeed())
            }
        }
    }

    private fun getEnvironment(player: EntityPlayer, rayTraceResult: RayTraceResult) = FairyCrystalDropEnvironment(player, player.world, rayTraceResult.blockPos, rayTraceResult.sideHit).also { environment ->
        environment.insertItemStacks(player) // インベントリ
        environment.insertBlocks(player.world, BlockRegion(rayTraceResult.blockPos.add(-2, -2, -2), rayTraceResult.blockPos.add(2, 2, 2))) // ワールドブロック
        environment.insertBiome(player.world.getBiome(rayTraceResult.blockPos)) // バイオーム
        environment.insertEntities(player.world, player.positionVector, 10.0) // エンティティ
    }

    private fun useCrystal(player: EntityPlayer, maxTimes: Int) {

        // プレイヤー視点判定
        val rayTraceResult = rayTrace(player.world, player, false) ?: return // ブロックに当たらなかった場合は無視
        if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) return // ブロックに当たらなかった場合は無視

        // ガチャ環境計算
        val environment = getEnvironment(player, rayTraceResult)

        repeat(maxTimes) {

            // 妖晶を得る
            val itemStackFairyCrystal = findItem(player) { itemStack -> itemStack.item is ItemFairyCrystal } ?: return // クリスタルを持ってない場合は無視
            val variantFairyCrystal = (itemStackFairyCrystal.item as ItemFairyCrystal).getVariant(itemStackFairyCrystal) ?: return // 異常なクリスタルを持っている場合は無視

            // ガチャリスト取得
            val commonBoost = variantFairyCrystal.getRateBoost(DropCategory.COMMON, player.proxy.skillContainer)
            val rareBoost = variantFairyCrystal.getRateBoost(DropCategory.RARE, player.proxy.skillContainer)
            val dropTable = environment.getDropTable(variantFairyCrystal.dropRank, commonBoost, rareBoost)

            // ガチャを引く
            val itemStackDrop = dropTable.getRandomItem(player.world.rand)?.orNull ?: return // ガチャが引けなかった場合は無視

            // 成立

            // ガチャアイテムを消費
            if (!player.isCreative) itemStackFairyCrystal.shrink(1)
            player.addStat(StatList.getObjectUseStats(itemStackFairyCrystal.item))

            // 妖精をドロップ
            val blockPos = rayTraceResult.blockPos.offset(rayTraceResult.sideHit)
            val entityItem = EntityItem(player.world, blockPos.x + 0.5, blockPos.y + 0.5, blockPos.z + 0.5, itemStackDrop.copy())
            entityItem.setNoPickupDelay()
            player.world.spawnEntity(entityItem)

        }

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
        }
    }
}
