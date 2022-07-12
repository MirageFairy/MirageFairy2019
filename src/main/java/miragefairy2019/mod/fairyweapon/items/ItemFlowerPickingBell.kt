package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.api.PickHandlerRegistry
import miragefairy2019.lib.MagicSelector
import miragefairy2019.lib.blocks
import miragefairy2019.lib.circle
import miragefairy2019.lib.doEffect
import miragefairy2019.lib.position
import miragefairy2019.lib.rayTraceBlock
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.drop
import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.artifacts.MirageFlower
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.boolean
import miragefairy2019.mod.fairyweapon.magic4.boost
import miragefairy2019.mod.fairyweapon.magic4.duration
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.integer
import miragefairy2019.mod.fairyweapon.magic4.magic
import miragefairy2019.mod.fairyweapon.magic4.percent0
import miragefairy2019.mod.fairyweapon.magic4.percent2
import miragefairy2019.mod.fairyweapon.magic4.pitch
import miragefairy2019.mod.fairyweapon.magic4.positive
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.fairyweapon.magic4.world
import miragefairy2019.mod.fairyweapon.playSound
import miragefairy2019.mod.fairyweapon.spawnParticleTargets
import miragefairy2019.mod.skill.Mastery
import mirrg.kotlin.hydrogen.atLeast
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.item.EntityXPOrb
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow

class ItemFlowerPickingBell(baseFortune: Double, extraItemDropRateFactor: Double) : ItemFairyWeaponMagic4() {
    val pitch = status("pitch", { 0.5.pow(costFactor - 1.0) }, { pitch })
    val maxTargetCount = status("maxTargetCount", { floor((8.0 + !Mana.DARK / 10.0 + !Erg.SUBMISSION / 5.0) * costFactor).toInt() atLeast 1 }, { integer })
    val fortune = status("fortune", { baseFortune + !Mana.SHINE / 5.0 + !Erg.HARVEST / 10.0 }, { float2 })
    val additionalReach = status("additionalReach", { 0.0 + !Mana.WIND / 20.0 + !Erg.SPACE / 10.0 atMost 30.0 }, { float2 })
    val radius = status("radius", { 4 + !Mana.GAIA / 20.0 + !Erg.SOUND / 10.0 atMost 20.0 }, { float2 })
    val wear = status("wear", { 0.25 / (1 + !Mana.FIRE / 40.0 + !Erg.SLASH / 20.0) }, { percent2 })
    val coolTime = status("coolTime", { 25.0 * costFactor }, { duration })
    val speedBoost = status("speedBoost", { 1.0 + !Mastery.flowerPicking / 100.0 }, { boost })
    val collection = status("collection", { !Erg.WARP >= 10 }, { boolean.positive })
    val extraItemDropRate = status("extraItemDropRate", { extraItemDropRateFactor }, { percent0 })
    val productionBoost = status("productionBoost", { 1.0 + !Mastery.flowerPicking / 100.0 }, { boost })

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = listOf("右クリックでミラージュフラワーを収穫") // TODO translate

    override fun getMagic() = magic {

        // 視線判定
        val rayTraceMagicSelector = MagicSelector.rayTraceBlock(world, player, additionalReach())

        // 視点判定
        val cursorMagicSelector = rayTraceMagicSelector.position

        // 妖精を持っていない場合、中止
        if (!hasPartnerFairy) return@magic object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(0xFF00FF) // 視点
            }
        }

        // 範囲判定
        val rangeMagicSelector = cursorMagicSelector.circle(radius())
        val targetsMagicSelector = rangeMagicSelector.blocks()

        // 対象計算
        val listTarget = targetsMagicSelector.item
            .sortedBy { it.second } // 近い順にソート
            .map { it.first } // BlockPosだけを抽出
            .asSequence() // 先頭から順番に判定
            .mapNotNull a@{ blockPos -> // Executorを取得
                val pickExecutor = PickHandlerRegistry.pickHandlers.asSequence()
                    .map { pickHandler -> pickHandler.getExecutor(world, blockPos, player) }
                    .filterNotNull()
                    .firstOrNull() ?: return@a null
                Pair(blockPos, pickExecutor)
            }
            .take(maxTargetCount()) // 最大個数を制限
            .toList() // リストにする

        // 資源がない場合、中止
        if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@magic object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(0xFF0000) // 視点
                rangeMagicSelector.item.doEffect() // 範囲
            }
        }

        // 発動対象がない場合、中止
        if (listTarget.isEmpty()) return@magic object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(0x00FFFF) // 視点
                rangeMagicSelector.item.doEffect() // 範囲
            }
        }

        // クールタイムが残っている場合、中止
        if (player.cooldownTracker.hasCooldown(this@ItemFlowerPickingBell)) return@magic object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(0xFFFF00) // 視点
                rangeMagicSelector.item.doEffect() // 範囲
                spawnParticleTargets(world, listTarget, { Vec3d(it.first).addVector(0.5, 0.5, 0.5) }, { 0xFFFF00 }) // 対象
            }
        }

        // 魔法成立
        object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(0x00FF00) // 視点
                rangeMagicSelector.item.doEffect() // 範囲
                spawnParticleTargets(world, listTarget, { Vec3d(it.first).addVector(0.5, 0.5, 0.5) }, { 0x00FF00 }) // 対象
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                var breakSound: SoundEvent? = null
                var collected = false
                var targetCount = 0

                run targets@{
                    for (pair in listTarget) {
                        val blockPos = pair.first
                        val pickExecutor = pair.second

                        if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@targets // 耐久が足りないので中止
                        if (targetCount + 1 > maxTargetCount()) return@targets // パワーが足りないので中止

                        // 成立

                        // 資源消費
                        weaponItemStack.damageItem(world.rand.randomInt(wear()), player)
                        targetCount++

                        // 音取得
                        run {
                            val blockState = world.getBlockState(blockPos)
                            breakSound = blockState.block.getSoundType(blockState, world, blockPos, player).breakSound
                        }

                        // 収穫
                        run {

                            // 収穫試行
                            val result = pickExecutor.tryPick(world.rand.randomInt(fortune()))
                            if (!result) return@targets

                            // 種の追加ドロップ
                            if (!world.isRemote) {
                                val count = world.rand.randomInt(extraItemDropRate() * productionBoost())
                                if (count > 0) MirageFlower.itemMirageFlowerSeeds().createItemStack(count = count).drop(world, Vec3d(blockPos).addVector(0.5, 0.5, 0.5)).setNoPickupDelay()
                            }

                            // 破壊したばかりのブロックの周辺のアイテムを集める
                            if (collection()) {
                                world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(blockPos)).forEach {
                                    collected = true
                                    it.setPosition(player.posX, player.posY, player.posZ)
                                    it.setNoPickupDelay()
                                }
                                world.getEntitiesWithinAABB(EntityXPOrb::class.java, AxisAlignedBB(blockPos)).forEach {
                                    collected = true
                                    it.setPosition(player.posX, player.posY, player.posZ)
                                }
                            }

                        }

                        // エフェクト
                        world.spawnParticle(
                            EnumParticleTypes.SPELL_MOB,
                            blockPos.x + 0.5,
                            blockPos.y + 0.5,
                            blockPos.z + 0.5,
                            (color shr 16 and 0xFF) / 255.0,
                            (color shr 8 and 0xFF) / 255.0,
                            (color shr 0 and 0xFF) / 255.0
                        )

                    }
                }

                if (targetCount >= 1) {

                    // エフェクト
                    playSound(world, player, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, pitch().toFloat())
                    world.playSound(null, player.posX, player.posY, player.posZ, breakSound!!, SoundCategory.PLAYERS, 1.0f, 1.0f)

                    // クールタイム
                    val ratio = targetCount / (maxTargetCount()).toDouble()
                    player.cooldownTracker.setCooldown(this@ItemFlowerPickingBell, ceil(coolTime() / speedBoost() * ratio.pow(0.5)).toInt())

                }
                if (collected) {

                    // エフェクト
                    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f)

                }

                return EnumActionResult.SUCCESS
            }
        }
    }
}
