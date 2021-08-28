package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.mod.api.ApiMirageFlower
import miragefairy2019.mod.api.fairy.IFairyType
import miragefairy2019.mod.api.magic.IMagicExecutor
import miragefairy2019.mod.api.magic.IMagicHandler
import miragefairy2019.mod.common.magic.MagicSelectorRayTrace
import miragefairy2019.modkt.api.erg.ErgTypes
import miragefairy2019.modkt.api.erg.ErgTypes.fell
import miragefairy2019.modkt.api.erg.ErgTypes.knowledge
import miragefairy2019.modkt.api.erg.IErgType
import miragefairy2019.modkt.api.magicstatus.IMagicStatus
import miragefairy2019.modkt.api.mana.IManaType
import miragefairy2019.modkt.api.mana.ManaTypes
import miragefairy2019.modkt.api.mana.ManaTypes.dark
import miragefairy2019.modkt.api.mana.ManaTypes.shine
import miragefairy2019.modkt.impl.getMana
import miragefairy2019.modkt.impl.magicstatus.*
import mirrg.boron.util.UtilsMath
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow

class ItemBellFlowerPicking(private val maxTargetCountFactor: Double, private val fortuneFactor: Double, private val radiusFactor: Double) : ItemFairyWeaponBase3() {
    fun IFairyType.mana(manaType: IManaType) = manas.getMana(manaType)
    fun IFairyType.erg(ergType: IErgType) = abilities.getPower(ergType)

    val pitch = "pitch" { -(cost / 50.0 - 1) * 12 }.shows { double2 }.positive().ranged(-12.0, 12.0)
    val maxTargetCount = "maxTargetCount" { floor(2 + mana(dark) * maxTargetCountFactor + erg(fell) * 0.1).toInt() }.shows { int }.positive().ranged(2, 10000)
    val fortune = "fortune" { 3 + mana(shine) * fortuneFactor + erg(knowledge) * 0.1 }.shows { double2 }.positive().ranged(3.0, 10000.0)
    val additionalReach = "additionalReach" { 0 + mana(ManaTypes.wind) * 0.1 }.shows { double2 }.positive().ranged(0.0, 10.0)
    val radius = "radius" { 4 + mana(ManaTypes.gaia) * radiusFactor }.shows { double2 }.positive().ranged(4.0, 10.0)
    val wear = "wear" { 0.2 / (1 + mana(ManaTypes.fire) * 0.03) }.shows { percent2 }.negative().ranged(0.0001, 0.2)
    val coolTime = "coolTime" { cost * 0.5 / (1 + mana(ManaTypes.aqua) * 0.03) }.shows { tick }.negative().ranged(0.0001, 100.0)
    val collection = "collection" { erg(ErgTypes.warp) >= 10 }.shows { boolean }

    override fun getMagicHandler(fairyType: IFairyType): IMagicHandler {
        operator fun <T> IMagicStatus<T>.invoke() = function.getValue(fairyType)
        return object : IMagicHandler {
            override fun getMagicStatuses() = listOf(
                    pitch,
                    maxTargetCount,
                    fortune,
                    additionalReach,
                    radius,
                    wear,
                    coolTime,
                    collection)

            override fun getMagicExecutor(world: World, player: EntityPlayer, itemStack: ItemStack): IMagicExecutor {

                // 視線判定
                val magicSelectorRayTrace = MagicSelectorRayTrace(world, player, additionalReach())

                // 視点判定
                val magicSelectorPosition = magicSelectorRayTrace.magicSelectorPosition

                // 妖精を持っていない場合、中止
                if (fairyType.isEmpty) return object : IMagicExecutor {
                    override fun onUpdate(itemSlot: Int, isSelected: Boolean) {
                        magicSelectorPosition.doEffect(0xFF00FF) // 視点
                    }
                }

                // 範囲判定
                val magicSelectorCircle = magicSelectorPosition.getMagicSelectorCircle(radius())

                // 対象計算
                val listTarget = magicSelectorCircle.blockPosList
                        .mapNotNull {
                            val blockState = world.getBlockState(it.blockPos)
                            val pickable = ApiMirageFlower.pickableRegistry[blockState.block].orElse(null)
                            if (pickable != null && pickable.isPickableAge(blockState)) Pair(it, pickable) else null
                        }
                        .sortedBy { it.first.distanceSquared }
                        .take(maxTargetCount())
                        .map { Pair(it.first.blockPos, it.second) }

                // 資源がない場合、中止
                if (itemStack.itemDamage + ceil(wear()).toInt() > itemStack.maxDamage) return object : IMagicExecutor {
                    override fun onUpdate(itemSlot: Int, isSelected: Boolean) {
                        magicSelectorPosition.doEffect(0xFF0000) // 視点
                        magicSelectorCircle.doEffect() // 範囲
                    }
                }

                // 発動対象がない場合、中止
                if (listTarget.isEmpty()) return object : IMagicExecutor {
                    override fun onUpdate(itemSlot: Int, isSelected: Boolean) {
                        magicSelectorPosition.doEffect(0x00FFFF) // 視点
                        magicSelectorCircle.doEffect() // 範囲
                    }
                }

                // クールタイムが残っている場合、中止
                if (player.cooldownTracker.hasCooldown(this@ItemBellFlowerPicking)) return object : IMagicExecutor {
                    override fun onUpdate(itemSlot: Int, isSelected: Boolean) {
                        magicSelectorPosition.doEffect(0xFFFF00) // 視点
                        magicSelectorCircle.doEffect() // 範囲
                        spawnParticleTargets(world, listTarget, { Vec3d(it.first).addVector(0.5, 0.5, 0.5) }, { 0xFFFF00 }) // 対象
                    }
                }

                // 魔法成立
                return object : IMagicExecutor {
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
                                if (itemStack.itemDamage + ceil(wear()).toInt() > itemStack.maxDamage) return@targets // 耐久が足りないので中止
                                if (targetCount + 1 > maxTargetCount()) return@targets // パワーが足りないので中止

                                // 成立

                                // 資源消費
                                itemStack.damageItem(UtilsMath.randomInt(world.rand, wear()), player)
                                targetCount++

                                // 音取得
                                run {
                                    val blockState = world.getBlockState(pair.first)
                                    breakSound = blockState.block.getSoundType(blockState, world, pair.first, player).breakSound
                                }

                                // 収穫
                                run {

                                    // 収穫試行
                                    val result = pair.second.tryPick(world, pair.first, Optional.of(player), UtilsMath.randomInt(world.rand, fortune()))
                                    if (!result) return@targets

                                    // 破壊したばかりのブロックの周辺のアイテムを集める
                                    if (collection()) {
                                        world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(pair.first)).forEach {
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
                                        pair.first.x + 0.5,
                                        pair.first.y + 0.5,
                                        pair.first.z + 0.5,
                                        (color shr 16 and 0xFF) / 255.0,
                                        (color shr 8 and 0xFF) / 255.0,
                                        (color shr 0 and 0xFF) / 255.0)

                            }
                        }

                        if (targetCount >= 1) {

                            // エフェクト
                            playSound(world, player, 2.0.pow(pitch() / 12.0))
                            world.playSound(null, player.posX, player.posY, player.posZ, breakSound!!, SoundCategory.PLAYERS, 1.0f, 1.0f)

                            // クールタイム
                            val ratio = targetCount / maxTargetCount().toDouble()
                            player.cooldownTracker.setCooldown(this@ItemBellFlowerPicking, (coolTime() * ratio.pow(0.5)).toInt())

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
}

fun playSound(world: World, player: EntityPlayer, pitch: Double) {
    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, pitch.toFloat())
}

fun <T> spawnParticleTargets(
        world: World,
        targets: List<T>,
        fPosition: (T) -> Vec3d,
        fColor: (T) -> Int) {
    val rate = 5 / targets.size.coerceAtLeast(5).toDouble()
    targets.forEach { target ->
        if (Math.random() < 0.2 * rate) {
            val position = fPosition(target)
            val color = fColor(target)
            world.spawnParticle(
                    EnumParticleTypes.SPELL_MOB,
                    position.x,
                    position.y,
                    position.z,
                    (color shr 16 and 0xFF) / 255.0,
                    (color shr 8 and 0xFF) / 255.0,
                    (color shr 0 and 0xFF) / 255.0)
        }
    }
}
