package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.lib.MagicSelector
import miragefairy2019.lib.doEffect
import miragefairy2019.lib.position
import miragefairy2019.lib.rayTraceBlock
import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.fairyweapon.MagicMessage
import miragefairy2019.mod.fairyweapon.breakBlock
import miragefairy2019.mod.fairyweapon.displayText
import miragefairy2019.mod.fairyweapon.magic4.FormulaArguments
import miragefairy2019.mod.fairyweapon.magic4.MagicArguments
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.magic
import miragefairy2019.mod.fairyweapon.magic4.world
import miragefairy2019.mod.fairyweapon.spawnParticleTargets
import mirrg.kotlin.hydrogen.atLeast
import net.minecraft.block.state.IBlockState
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.ceil

abstract class ItemMiragiumToolBase() : ItemFairyWeaponMagic4() {
    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = listOf("右クリックでブロックを破壊") // TRANSLATE

    override fun getMagic() = magic {
        val rayTraceMagicSelector = MagicSelector.rayTraceBlock(world, player, getAdditionalReach(this)) // 視線判定
        val cursorMagicSelector = rayTraceMagicSelector.position // 視点判定

        fun fail(color: Int, magicMessage: MagicMessage) = object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(color)
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (!world.isRemote) player.sendStatusMessage(magicMessage.displayText, true)
                return EnumActionResult.FAIL
            }
        }

        if (!hasPartnerFairy) return@magic fail(0xFF00FF, MagicMessage.NO_FAIRY) // パートナー妖精判定
        val blockPos = rayTraceMagicSelector.item.rayTraceWrapper.let { if (focusSurface()) it.surfaceBlockPos else it.blockPos }
        if (weaponItemStack.itemDamage + ceil(getDurabilityCost(this, world, blockPos, world.getBlockState(blockPos))).toInt() > weaponItemStack.maxDamage) return@magic fail(0xFF0000, MagicMessage.INSUFFICIENT_DURABILITY) // 耐久判定
        val targets = iterator {
            val iterator = getTargetBlockPoses(this@magic, blockPos)
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (canBreak(this@magic, next)) yield(next)
            }
        } // 対象判定
        if (!targets.hasNext()) return@magic fail(0x00FFFF, MagicMessage.NO_TARGET) // ターゲットなし判定
        if (player.cooldownTracker.hasCooldown(weaponItem)) return@magic fail(0xFFFF00, MagicMessage.COOL_TIME) // クールタイム判定

        // 魔法成立
        object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(0xFFFFFF)
                spawnParticleTargets(world, targets.asSequence().toList().map { Vec3d(it).addVector(0.5, 0.5, 0.5) })
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (world.isRemote) { // クライアントワールドの場合、腕を振るだけ
                    player.swingArm(hand) // エフェクト
                    return EnumActionResult.SUCCESS
                }

                // 行使
                val coolTimePerHardness = getCoolTimePerHardness(this@magic)
                var breakSound: SoundEvent? = null
                var count = 0
                var actualCoolTime = 0.0
                run breakExecution@{
                    targets.forEach { target ->
                        val blockState = world.getBlockState(target)

                        val damage = world.rand.randomInt(getDurabilityCost(this@magic, world, target, blockState)) // 耐久コスト
                        if (weaponItemStack.itemDamage + damage > weaponItemStack.maxDamage) return@breakExecution // 耐久不足なら終了

                        // 破壊成立
                        weaponItemStack.damageItem(damage, player)
                        actualCoolTime += coolTimePerHardness * getActualBlockHardness(world, target, blockState)
                        breakSound = blockState.block.getSoundType(blockState, world, target, player).breakSound
                        breakBlock(
                            world = world,
                            player = player,
                            itemStack = weaponItemStack,
                            blockPos = target,
                            fortune = world.rand.randomInt(getFortune(this@magic)),
                            silkTouch = isSilkTouch(this@magic),
                            collection = doCollection(this@magic),
                            canShear = isShearing(this@magic)
                        )
                        count++
                    }
                }

                // 破壊時
                if (count > 0) {
                    if (doCollection(this@magic)) world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f)
                    breakSound?.let { world.playSound(null, player.posX, player.posY, player.posZ, it, player.soundCategory, 1.0f, 1.0f) } // エフェクト
                    player.cooldownTracker.setCooldown(this@ItemMiragiumToolBase, ceil(actualCoolTime + 10.0).toInt()) // クールタイム
                }

                // エフェクト
                world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.soundCategory, 1.0f, 1.0f) // 魔法のSE
                player.spawnSweepParticles() // スイングのパーティクル

                return EnumActionResult.SUCCESS
            }
        }
    }

    open fun getAdditionalReach(a: MagicArguments) = 0.0

    open fun focusSurface() = false

    abstract val maxHardness: FormulaArguments.() -> Double // TODO -> function

    @Suppress("SimplifyBooleanWithConstants")
    open fun canBreak(a: MagicArguments, blockPos: BlockPos) = when {
        !isEffective(a.weaponItemStack, a.world.getBlockState(blockPos)) -> false // 効果的でなければならない
        a.world.getBlockState(blockPos).getBlockHardness(a.world, blockPos) < 0 -> false // 岩盤であってはならない
        a.world.getBlockState(blockPos).getBlockHardness(a.world, blockPos) > a.maxHardness() -> false // 硬すぎてはいけない
        else -> true
    }

    /** このイテレータは破壊処理中に逐次的に呼び出されるパターンと、破壊前に一括で呼び出されるパターンがあります。 */
    open fun getTargetBlockPoses(a: MagicArguments, baseBlockPos: BlockPos): Iterator<BlockPos> = iterator { yield(baseBlockPos) }

    open fun getDurabilityCost(a: FormulaArguments, world: World, blockPos: BlockPos, blockState: IBlockState) = 1.0 / 8.0

    open fun getActualBlockHardness(world: World, blockPos: BlockPos, blockState: IBlockState) = blockState.getBlockHardness(world, blockPos).toDouble() atLeast 1.0
    open fun getCoolTimePerHardness(a: MagicArguments) = 20.0

    open fun getFortune(a: FormulaArguments) = 0.0

    open fun isSilkTouch(a: FormulaArguments) = false

    open fun isShearing(a: FormulaArguments) = false

    open fun doCollection(a: FormulaArguments) = false
}
