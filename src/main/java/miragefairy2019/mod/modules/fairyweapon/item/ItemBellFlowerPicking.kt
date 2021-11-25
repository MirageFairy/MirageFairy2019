package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.drop
import miragefairy2019.mod.common.magic.MagicSelectorRayTrace
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWeaponBase3.Companion.EnumVisibility.ALWAYS
import miragefairy2019.mod3.erg.api.EnumErgType
import miragefairy2019.mod3.erg.api.EnumErgType.WARP
import miragefairy2019.mod3.magic.api.IMagicHandler
import miragefairy2019.mod3.magic.negative
import miragefairy2019.mod3.magic.positive
import miragefairy2019.mod3.magic.positiveBoolean
import miragefairy2019.mod3.mana.api.EnumManaType
import miragefairy2019.mod3.mirageflower.api.ApiMirageFlower
import miragefairy2019.mod3.skill.EnumMastery
import mirrg.boron.util.UtilsMath
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.Vec3d
import java.util.Optional
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow

class ItemBellFlowerPicking(weaponStrength: Double, weaponExtent: Double, weaponEndurance: Double, weaponProduction: Double, maxExtraItemDropRate: Double) :
    ItemFairyWeaponBase3(
        EnumManaType.DARK, EnumMastery.flowerPicking,
        weaponStrength, weaponExtent, weaponEndurance, weaponProduction,
        EnumErgType.SOUND, EnumErgType.SPACE, EnumErgType.SLASH, EnumErgType.HARVEST
    ) {
    val pitch = "pitch"({ double2.positive }) { -(cost / 50.0 - 1) * 12 }.setRange(-12.0..12.0)
    val maxTargetCount = "maxTargetCount"({ int.positive }) { 2 + floor(+!strength * 0.1).toInt() }.setRange(1..100)
    val fortune = "fortune"({ double2.positive }) { 3 + !production * 0.1 }.setRange(0.0..100.0)
    val additionalReach = "additionalReach"({ double2.positive }) { !extent * 0.1 }.setRange(0.0..10.0)
    val radius = "radius"({ double2.positive }) { 4 + !extent * 0.05 }.setRange(0.0..10.0)
    val wear = "wear"({ percent2.negative }) { 1.0 / (1 + !endurance * 0.03) }
    val coolTime = "coolTime"({ tick.negative }) { cost * 0.5 }
    val collection = "collection"({ boolean.positiveBoolean }) { !WARP >= 10 }.setVisibility(ALWAYS)
    val extraItemDropRate = "extraItemDropRate"({ percent1.positive }) { skillFunction1(mastery, 0, 100, 0.1, maxExtraItemDropRate) }.setVisibility(ALWAYS)

    init {
        magic {

            // 視線判定
            val magicSelectorRayTrace = MagicSelectorRayTrace(world, player, !additionalReach)

            // 視点判定
            val magicSelectorPosition = magicSelectorRayTrace.magicSelectorPosition

            // 妖精を持っていない場合、中止
            if (fairyType.isEmpty) return@magic object : IMagicHandler {
                override fun onUpdate(itemSlot: Int, isSelected: Boolean) {
                    magicSelectorPosition.doEffect(0xFF00FF) // 視点
                }
            }

            // 範囲判定
            val magicSelectorCircle = magicSelectorPosition.getMagicSelectorCircle(!radius)

            // 対象計算
            val listTarget = magicSelectorCircle.blockPosList
                .mapNotNull {
                    val blockState = world.getBlockState(it.blockPos)
                    val pickable = ApiMirageFlower.pickableRegistry[blockState.block].orElse(null)
                    if (pickable != null && pickable.isPickableAge(blockState)) Pair(it, pickable) else null
                }
                .sortedBy { it.first.distanceSquared }
                .take(!maxTargetCount)
                .map { Pair(it.first.blockPos, it.second) }

            // 資源がない場合、中止
            if (itemStack.itemDamage + ceil(!wear).toInt() > itemStack.maxDamage) return@magic object : IMagicHandler {
                override fun onUpdate(itemSlot: Int, isSelected: Boolean) {
                    magicSelectorPosition.doEffect(0xFF0000) // 視点
                    magicSelectorCircle.doEffect() // 範囲
                }
            }

            // 発動対象がない場合、中止
            if (listTarget.isEmpty()) return@magic object : IMagicHandler {
                override fun onUpdate(itemSlot: Int, isSelected: Boolean) {
                    magicSelectorPosition.doEffect(0x00FFFF) // 視点
                    magicSelectorCircle.doEffect() // 範囲
                }
            }

            // クールタイムが残っている場合、中止
            if (player.cooldownTracker.hasCooldown(this@ItemBellFlowerPicking)) return@magic object : IMagicHandler {
                override fun onUpdate(itemSlot: Int, isSelected: Boolean) {
                    magicSelectorPosition.doEffect(0xFFFF00) // 視点
                    magicSelectorCircle.doEffect() // 範囲
                    spawnParticleTargets(world, listTarget, { Vec3d(it.first).addVector(0.5, 0.5, 0.5) }, { 0xFFFF00 }) // 対象
                }
            }

            // 魔法成立
            object : IMagicHandler {
                override fun onUpdate(itemSlot: Int, isSelected: Boolean) {
                    magicSelectorPosition.doEffect(0x00FF00) // 視点
                    magicSelectorCircle.doEffect() // 範囲
                    spawnParticleTargets(world, listTarget, { Vec3d(it.first).addVector(0.5, 0.5, 0.5) }, { 0x00FF00 }) // 対象
                }

                override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                    var breakSound: SoundEvent? = null
                    var collected = false
                    var targetCount = 0

                    run targets@{
                        for (pair in listTarget) {
                            val blockPos = pair.first
                            val pickable = pair.second

                            if (itemStack.itemDamage + ceil(!wear).toInt() > itemStack.maxDamage) return@targets // 耐久が足りないので中止
                            if (targetCount + 1 > !maxTargetCount) return@targets // パワーが足りないので中止

                            // 成立

                            // 資源消費
                            itemStack.damageItem(UtilsMath.randomInt(world.rand, !wear), player)
                            targetCount++

                            // 音取得
                            run {
                                val blockState = world.getBlockState(blockPos)
                                breakSound = blockState.block.getSoundType(blockState, world, blockPos, player).breakSound
                            }

                            // 収穫
                            run {

                                // 収穫試行
                                val result = pickable.tryPick(world, blockPos, Optional.of(player), UtilsMath.randomInt(world.rand, !fortune))
                                if (!result) return@targets

                                // 種の追加ドロップ
                                if (!extraItemDropRate > world.rand.nextDouble()) {
                                    if (!world.isRemote) drop(world, ItemStack(ApiMirageFlower.itemMirageFlowerSeeds), Vec3d(blockPos).addVector(0.5, 0.5, 0.5)).setNoPickupDelay()
                                }

                                // 破壊したばかりのブロックの周辺のアイテムを集める
                                if (!collection) {
                                    world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(blockPos)).forEach {
                                        collected = true
                                        it.setPosition(player.posX, player.posY, player.posZ)
                                        it.setNoPickupDelay()
                                    }
                                }

                            }

                            // エフェクト
                            val color = fairyType.color
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
                        playSound(world, player, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0.pow(!pitch / 12.0).toFloat())
                        world.playSound(null, player.posX, player.posY, player.posZ, breakSound!!, SoundCategory.PLAYERS, 1.0f, 1.0f)

                        // クールタイム
                        val ratio = targetCount / (!maxTargetCount).toDouble()
                        player.cooldownTracker.setCooldown(this@ItemBellFlowerPicking, (!coolTime * ratio.pow(0.5)).toInt())

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
}
