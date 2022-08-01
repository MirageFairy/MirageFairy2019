package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.lib.proxy
import miragefairy2019.lib.skillContainer
import miragefairy2019.libkt.BlockRegion
import miragefairy2019.libkt.drop
import miragefairy2019.libkt.getRandomItem
import miragefairy2019.libkt.notEmptyOrNull
import miragefairy2019.libkt.randomInt
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.artifacts.ItemFairyCrystal
import miragefairy2019.mod.artifacts.getRateBoost
import miragefairy2019.mod.fairy.FairyCard
import miragefairy2019.mod.fairy.createItemStack
import miragefairy2019.mod.fairyweapon.findItem
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.integer
import miragefairy2019.mod.fairyweapon.magic4.magic
import miragefairy2019.mod.fairyweapon.magic4.map
import miragefairy2019.mod.fairyweapon.magic4.percent2
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.fairyweapon.magic4.world
import miragefairy2019.mod.systems.DropCategory
import miragefairy2019.mod.systems.FairyCrystalDropEnvironment
import miragefairy2019.mod.systems.getDropTable
import miragefairy2019.mod.systems.insertBiome
import miragefairy2019.mod.systems.insertBlocks
import miragefairy2019.mod.systems.insertEntities
import miragefairy2019.mod.systems.insertItemStacks
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemStack
import net.minecraft.stats.StatList
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.ceil

class ItemPrayerWheel(baseFortune: Double, private val maxTryCountPerTick: Int) : ItemFairyWeaponMagic4() {
    val maxTryCount = status("maxSpeed", { maxTryCountPerTick }, { integer.map { textComponent { "${value * 20} Hz"() } } })
    val fortune = status("fortune", { baseFortune + !Mana.SHINE / 100.0 + !Erg.CRYSTAL / 100.0 + !Erg.SUBMISSION / 50.0 }, { float2 })
    val wear = status("wear", { 0.01 / (1.0 + !Mana.FIRE / 40.0 + !Erg.SOUND / 20.0) }, { percent2 })

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = listOf("右クリック長押しでフェアリークリスタルを高速消費") // TRANSLATE Hold right mouse button to use fairy crystals quickly


    override fun getItemUseAction(stack: ItemStack) = EnumAction.BOW
    override fun getMaxItemUseDuration(stack: ItemStack) = 72000 // 永続

    override fun getMagic() = magic {
        object : MagicHandler() {
            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                player.activeHand = hand
                return EnumActionResult.SUCCESS
            }

            override fun onUsingTick(count: Int) {
                if (world.isRemote) return

                // プレイヤー視点判定
                val rayTraceResult = rayTrace(world, player, false) ?: return // ブロックに当たらなかった場合は無視
                if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) return // ブロックに当たらなかった場合は無視

                // ガチャ環境計算
                val environment = getEnvironment(player, rayTraceResult)

                repeat(getTryCount(count) atMost maxTryCount()) {

                    if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return // 耐久がないなら中断

                    // フェアリークリスタルを得る
                    val itemStackFairyCrystal = findItem(player) { itemStack -> itemStack.item is ItemFairyCrystal } ?: return // クリスタルを持ってない場合は無視
                    val variantFairyCrystal = (itemStackFairyCrystal.item as ItemFairyCrystal).getVariant(itemStackFairyCrystal) ?: return // 異常なクリスタルを持っている場合は無視

                    // ガチャリスト取得
                    val commonBoost = variantFairyCrystal.getRateBoost(DropCategory.COMMON, player.proxy.skillContainer)
                    val rareBoost = variantFairyCrystal.getRateBoost(DropCategory.RARE, player.proxy.skillContainer)
                    val dropTable = environment.getDropTable(variantFairyCrystal.dropRank, commonBoost, rareBoost)

                    // 消費
                    weaponItemStack.damageItem(world.rand.randomInt(wear()), player) // 耐久
                    if (!player.isCreative) itemStackFairyCrystal.shrink(1) // クリスタル
                    player.addStat(StatList.getObjectUseStats(itemStackFairyCrystal.item)!!)

                    // ドロップ
                    repeat(world.rand.randomInt(1.0 + fortune())) {
                        val itemStackDrop = dropTable.getRandomItem(world.rand)?.notEmptyOrNull ?: FairyCard.AIR.createItemStack(variantFairyCrystal.dropRank) // ガチャを引く
                        val blockPos = rayTraceResult.blockPos.offset(rayTraceResult.sideHit) // 出現座標
                        itemStackDrop.drop(world, Vec3d(blockPos).addVector(0.5, 0.5, 0.5), noPickupDelay = true) // 妖精をドロップ
                    }

                }

            }
        }
    }

    private fun getEnvironment(player: EntityPlayer, rayTraceResult: RayTraceResult) = FairyCrystalDropEnvironment(player, player.world, rayTraceResult.blockPos, rayTraceResult.sideHit).also { environment ->
        environment.insertItemStacks(player) // インベントリ
        environment.insertBlocks(player.world, BlockRegion(rayTraceResult.blockPos.add(-2, -2, -2), rayTraceResult.blockPos.add(2, 2, 2))) // ワールドブロック
        environment.insertBiome(player.world.getBiome(rayTraceResult.blockPos)) // バイオーム
        environment.insertEntities(player.world, player.positionVector, 10.0) // エンティティ
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
