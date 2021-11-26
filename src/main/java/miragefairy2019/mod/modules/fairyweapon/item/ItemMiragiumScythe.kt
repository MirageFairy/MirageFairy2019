package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod.api.fairyweapon.formula.ApiFormula
import miragefairy2019.mod.api.fairyweapon.formula.IMagicStatus
import miragefairy2019.mod.modules.fairyweapon.magic.EnumTargetExecutability
import miragefairy2019.mod.modules.fairyweapon.magic.MagicExecutor
import miragefairy2019.mod.modules.fairyweapon.magic.SelectorRayTrace
import miragefairy2019.mod.modules.fairyweapon.magic.UtilsMagic
import mirrg.boron.util.UtilsMath
import net.minecraft.block.material.Material
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.math.ceil

class ItemMiragiumScythe : ItemFairyWeaponBase2() {
    var wear: IMagicStatus<Double> = registerMagicStatus("wear", ApiFormula.formatterPercent1(), ApiFormula.`val`(1 / 25.0))
    var coolTime: IMagicStatus<Double> = registerMagicStatus("coolTime", ApiFormula.formatterTick(), ApiFormula.`val`(20.0))

    init {
        addInformationHandlerFunctions("Right click: use magic") // TODO translate
    }

    override fun getExecutor(world: World, itemStack: ItemStack, player: EntityPlayer): MagicExecutor {

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
        if (fairyType.isEmpty) return object : MagicExecutor() {
            override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
                selectorRayTrace.doEffect(0xFF00FF)
            }
        }

        // 材料なし判定
        if (itemStack.itemDamage + ceil(wear[fairyType]).toInt() > itemStack.maxDamage) return object : MagicExecutor() {
            override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
                selectorRayTrace.doEffect(0xFF0000)
            }
        }

        // ターゲットなし判定
        if (blockPoses.isEmpty()) return object : MagicExecutor() {
            override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
                selectorRayTrace.doEffect(0x00FFFF)
            }
        }

        // クールダウン判定
        if (player.cooldownTracker.hasCooldown(this)) return object : MagicExecutor() {
            override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
                selectorRayTrace.doEffect(0xFFFF00)
            }
        }

        // 発動可能
        return object : MagicExecutor() {
            override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
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
                                val damage = UtilsMath.randomInt(world.rand, wear[fairyType])

                                // 耐久不足
                                if (itemStack.itemDamage + damage > itemStack.maxDamage) return@a

                                // 発動
                                itemStack.damageItem(damage, player)
                                breakBlock(world, player, EnumFacing.UP, itemStack, blockPos, 0, false)
                                c++

                            }
                        }
                        c
                    }

                    if (count > 0) {

                        // エフェクト
                        world.playSound(null, player.posX, player.posY, player.posZ, breakSound, player.soundCategory, 1.0f, 1.0f)

                        // クールタイム
                        player.cooldownTracker.setCooldown(this@ItemMiragiumScythe, coolTime[fairyType].toInt())

                    }

                    // エフェクト
                    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.soundCategory, 1.0f, 1.0f)
                    player.spawnSweepParticles()

                } else {

                    // エフェクト
                    player.swingArm(hand)

                }
                return ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand))
            }

            override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
                selectorRayTrace.doEffect(0xFFFFFF)
                UtilsMagic.spawnParticleTargets(world, blockPoses.map { Pair(Vec3d(it).addVector(0.5, 0.5, 0.5), EnumTargetExecutability.EFFECTIVE) })
            }
        }
    }

    private fun getTargets(world: World, blockPos: BlockPos): List<BlockPos> {
        val tuples = mutableListOf<Pair<BlockPos, Double>>()
        (-2..2).forEach { xi ->
            (-0..0).forEach { yi ->
                (-2..2).forEach { zi ->
                    val blockPos2 = blockPos.add(xi, yi, zi)
                    val blockState = world.getBlockState(blockPos2)
                    when (blockState.material) {
                        Material.PLANTS, Material.LEAVES, Material.VINE, Material.GRASS, Material.CACTUS -> {
                            if (blockState.getBlockHardness(world, blockPos2) == 0f) {
                                tuples += Pair(blockPos2, blockPos2.distanceSq(blockPos))
                            }
                        }
                    }
                }
            }
        }
        return tuples.sortedBy { it.second }.map { it.first }
    }

}
