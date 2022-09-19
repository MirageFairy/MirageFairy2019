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
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.ceil

abstract class ItemMiragiumToolBase() : ItemFairyWeaponMagic4() {
    abstract val maxHardness: FormulaArguments.() -> Double
    abstract val actualFortune: FormulaArguments.() -> Double
    abstract val wear: FormulaArguments.() -> Double
    open val collection: FormulaArguments.() -> Boolean = { false }
    open val silkTouch: FormulaArguments.() -> Boolean = { false }
    open val shearing: FormulaArguments.() -> Boolean = { false }

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = listOf("右クリックでブロックを破壊") // TRANSLATE

    override fun getMagic() = magic {
        val rayTraceMagicSelector = MagicSelector.rayTraceBlock(world, player, 0.0) // 視線判定
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
        if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@magic fail(0xFF0000, MagicMessage.INSUFFICIENT_DURABILITY) // 耐久判定
        val blockPos = rayTraceMagicSelector.item.rayTraceWrapper.surfaceBlockPos
        val targets = iterateTargets(this, blockPos) // 対象判定
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
                val actualCoolTimePerBlock = getActualCoolTimePerBlock(this@magic)
                var breakSound: SoundEvent? = null
                var count = 0
                var actualCoolTime = 0.0
                run breakExecution@{
                    targets.forEach { target ->
                        val damage = world.rand.randomInt(wear()) // 耐久コスト
                        if (weaponItemStack.itemDamage + damage > weaponItemStack.maxDamage) return@breakExecution // 耐久不足なら終了

                        // 破壊成立
                        weaponItemStack.damageItem(damage, player)
                        val blockState = world.getBlockState(target)
                        actualCoolTime += actualCoolTimePerBlock * (blockState.getBlockHardness(world, target).toDouble() atLeast 1.0)
                        breakSound = blockState.block.getSoundType(blockState, world, target, player).breakSound
                        breakBlock(
                            world = world,
                            player = player,
                            itemStack = weaponItemStack,
                            blockPos = target,
                            fortune = world.rand.randomInt(actualFortune()),
                            silkTouch = silkTouch(),
                            collection = collection(),
                            canShear = shearing()
                        )
                        count++
                    }
                }

                // 破壊時
                if (count > 0) {
                    if (collection()) world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f)
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

    /**
     * このイテレータは破壊処理中に逐次的に呼び出されるパターンと、破壊前に一括で呼び出されるパターンがあります。
     * 内部で必ず[canBreak]による破壊可能判定を行わなければなりません。
     */
    abstract fun iterateTargets(magicArguments: MagicArguments, blockPosBase: BlockPos): Iterator<BlockPos>
    abstract fun getActualCoolTimePerBlock(magicArguments: MagicArguments): Double

    @Suppress("SimplifyBooleanWithConstants")
    open fun canBreak(magicArguments: MagicArguments, blockPos: BlockPos) = true
        && magicArguments.world.getBlockState(blockPos).getBlockHardness(magicArguments.world, blockPos) >= 0 // 岩盤であってはならない
        && isEffective(magicArguments.weaponItemStack, magicArguments.world.getBlockState(blockPos)) // 効果的でなければならない
        && magicArguments.world.getBlockState(blockPos).getBlockHardness(magicArguments.world, blockPos) <= magicArguments.maxHardness() // 硬すぎてはいけない
}
