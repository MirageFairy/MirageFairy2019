package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWeaponBase3.Companion.MagicScope
import miragefairy2019.mod.modules.fairyweapon.magic.EnumTargetExecutability
import miragefairy2019.mod.modules.fairyweapon.magic.SelectorRayTrace
import miragefairy2019.mod.modules.fairyweapon.magic.UtilsMagic
import miragefairy2019.mod3.erg.api.EnumErgType
import miragefairy2019.mod3.magic.api.IMagicHandler
import miragefairy2019.mod3.magic.negative
import miragefairy2019.mod3.magic.positive
import miragefairy2019.mod3.mana.api.EnumManaType
import miragefairy2019.mod3.skill.api.IMastery
import net.minecraft.block.state.IBlockState
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.math.ceil

abstract class ItemMiragiumToolBase(
    weaponManaType: EnumManaType,
    mastery: IMastery,
    additionalBaseStatus: Double,
    val breakSpeed: Float
) : ItemFairyWeaponBase3(
    weaponManaType,
    mastery
) {
    val strength = createStrengthStatus(additionalBaseStatus, EnumErgType.SLASH)
    val extent = createExtentStatus(additionalBaseStatus, EnumErgType.SHOOT)
    val endurance = createEnduranceStatus(additionalBaseStatus, EnumErgType.SENSE)
    val production = createProductionStatus(additionalBaseStatus, EnumErgType.HARVEST)
    val cost = createCostStatus()

    val maxHardness = "maxHardness"({ double2.positive }) { !strength * 0.01 }.setRange(0.0..10.0).setVisibility(Companion.EnumVisibility.DETAIL)
    val fortune = "fortune"({ double2.positive }) { !production * 0.03 }.setRange(0.0..100.0).setVisibility(Companion.EnumVisibility.DETAIL)
    val range = "range"({ int.positive }) { (2 + !extent * 0.02).toInt() }.setRange(2..5).setVisibility(Companion.EnumVisibility.DETAIL)
    val wear = "wear"({ percent2.positive }) { 1 / (25.0 + !endurance * 0.25) }.setVisibility(Companion.EnumVisibility.DETAIL)
    val coolTime = "coolTime"({ tick.negative }) { cost * 0.3 }.setVisibility(Companion.EnumVisibility.DETAIL)

    override fun getDestroySpeed(itemStack: ItemStack, blockState: IBlockState) = if (isEffective(blockState)) breakSpeed else 1.0f
    override fun canHarvestBlock(blockState: IBlockState) = isEffective(blockState)

    init {
        addInformationHandlerFunctions("Right click: use magic") // TODO translate

        magic {

            // 妖精取得
            val fairyType = findFairy(itemStack, player).orElse(null)?.let { it.y!! } ?: ApiFairy.empty()!!

            // 視線判定
            val selectorRayTrace = SelectorRayTrace(world, player, 0.0)

            // 対象判定
            val blockPoses = if (selectorRayTrace.sideHit.isPresent) {
                getTargets(world, selectorRayTrace.blockPos.offset(selectorRayTrace.sideHit.get()))
            } else {
                getTargets(world, selectorRayTrace.blockPos)
            }

            // 妖精なし判定
            if (fairyType.isEmpty) return@magic object : IMagicHandler {
                override fun onUpdate(itemSlot: Int, isSelected: Boolean) {
                    selectorRayTrace.doEffect(0xFF00FF)
                }
            }

            // 材料なし判定
            if (itemStack.itemDamage + ceil(!wear).toInt() > itemStack.maxDamage) return@magic object : IMagicHandler {
                override fun onUpdate(itemSlot: Int, isSelected: Boolean) {
                    selectorRayTrace.doEffect(0xFF0000)
                }
            }

            // ターゲットなし判定
            if (blockPoses.isEmpty()) return@magic object : IMagicHandler {
                override fun onUpdate(itemSlot: Int, isSelected: Boolean) {
                    selectorRayTrace.doEffect(0x00FFFF)
                }
            }

            // クールダウン判定
            if (player.cooldownTracker.hasCooldown(this@ItemMiragiumToolBase)) return@magic object : IMagicHandler {
                override fun onUpdate(itemSlot: Int, isSelected: Boolean) {
                    selectorRayTrace.doEffect(0xFFFF00)
                }
            }

            // 発動可能
            object : IMagicHandler {
                override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                    if (!world.isRemote) {

                        // 音取得
                        val breakSound: SoundEvent = run {
                            val blockPos = blockPoses[0]
                            val blockState = world.getBlockState(blockPos)
                            blockState.block.getSoundType(blockState, world, blockPos, player).breakSound
                        }

                        val count = run {
                            var c = 0
                            run a@{
                                blockPoses.forEach { blockPos ->

                                    // 耐久コスト
                                    val damage = world.rand.randomInt(!wear)

                                    // 耐久不足
                                    if (itemStack.itemDamage + damage > itemStack.maxDamage) return@a

                                    // 発動
                                    itemStack.damageItem(damage, player)
                                    breakBlock(world, player, EnumFacing.UP, itemStack, blockPos, world.rand.randomInt(!fortune), false)
                                    c++

                                }
                            }
                            c
                        }

                        if (count > 0) {

                            // エフェクト
                            world.playSound(null, player.posX, player.posY, player.posZ, breakSound, player.soundCategory, 1.0f, 1.0f)

                            // クールタイム
                            player.cooldownTracker.setCooldown(this@ItemMiragiumToolBase, (!coolTime).toInt())

                        }

                        // エフェクト
                        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.soundCategory, 1.0f, 1.0f)
                        player.spawnSweepParticles()

                    } else {

                        // エフェクト
                        player.swingArm(hand)

                    }
                    return EnumActionResult.SUCCESS
                }

                override fun onUpdate(itemSlot: Int, isSelected: Boolean) {
                    selectorRayTrace.doEffect(0xFFFFFF)
                    UtilsMagic.spawnParticleTargets(world, blockPoses.map { Pair(Vec3d(it).addVector(0.5, 0.5, 0.5), EnumTargetExecutability.EFFECTIVE) })
                }
            }
        }
    }

    abstract fun MagicScope.getTargets(world: World, blockPosBase: BlockPos): List<BlockPos>
    abstract fun isEffective(state: IBlockState): Boolean
}
