package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.lib.EMPTY_FAIRY
import miragefairy2019.lib.MagicSelector
import miragefairy2019.lib.rayTrace
import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.fairyweapon.EnumTargetExecutability
import miragefairy2019.mod.fairyweapon.breakBlock
import miragefairy2019.mod.fairyweapon.findFairy
import miragefairy2019.mod.fairyweapon.magic4.EnumVisibility
import miragefairy2019.mod.fairyweapon.magic4.MagicArguments
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.magic
import miragefairy2019.mod.fairyweapon.magic4.percent2
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.fairyweapon.magic4.world
import miragefairy2019.mod.fairyweapon.spawnParticle
import miragefairy2019.mod.fairyweapon.spawnParticleTargets
import miragefairy2019.mod.skill.Mastery
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.ceil

fun MagicArguments.fail(cursor: Vec3d, color: Int) = object : MagicHandler() {
    override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) = spawnParticle(world, cursor, color)
}

abstract class ItemMiragiumToolBase(
    weaponMana: Mana,
    mastery: Mastery,
    additionalBaseStatus: Double
) : ItemFairyWeaponBase3(
    weaponMana,
    mastery
) {
    val strength = createStrengthStatus(additionalBaseStatus, Erg.SLASH)
    val extent = createExtentStatus(additionalBaseStatus, Erg.SHOOT)
    val endurance = createEnduranceStatus(additionalBaseStatus, Erg.SENSE)
    val production = createProductionStatus(additionalBaseStatus, Erg.HARVEST)
    val cost = createCostStatus()

    val fortune = status("fortune", { !production * 0.03 }, { float2 }) { setRange(0.0..100.0).setVisibility(EnumVisibility.DETAIL) }
    val wear = status("wear", { 1 / (25.0 + !endurance * 0.25) }, { percent2 }) { setVisibility(EnumVisibility.DETAIL) }

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = "右クリックでブロックを破壊" // TODO translate

    override fun getMagic() = magic {
        val fairyType = findFairy(weaponItemStack, player)?.second ?: EMPTY_FAIRY // 妖精取得
        val selectorRayTrace = MagicSelector.rayTrace(world, player, 0.0) // 視線判定
        if (fairyType.isEmpty) return@magic fail(selectorRayTrace.item.position, 0xFF00FF) // 妖精なし判定
        if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@magic fail(selectorRayTrace.item.position, 0xFF0000) // 材料なし判定
        val targets = selectorRayTrace.item.blockPos.let { if (selectorRayTrace.item.sideHit != null) it.offset(selectorRayTrace.item.sideHit!!) else it }.let { iterateTargets(this@magic, it) } // 対象判定
        if (!targets.hasNext()) return@magic fail(selectorRayTrace.item.position, 0x00FFFF) // ターゲットなし判定
        if (player.cooldownTracker.hasCooldown(this@ItemMiragiumToolBase)) return@magic fail(selectorRayTrace.item.position, 0xFFFF00) // クールダウン判定

        object : MagicHandler() { // 行使可能
            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (world.isRemote) { // クライアントワールドの場合、腕を振るだけ
                    player.swingArm(hand) // エフェクト
                    return EnumActionResult.SUCCESS
                }

                // 行使
                var breakSound: SoundEvent? = null
                var count = 0
                run breakExecution@{
                    targets.forEach { target ->
                        val damage = world.rand.randomInt(wear()) // 耐久コスト
                        if (weaponItemStack.itemDamage + damage > weaponItemStack.maxDamage) return@breakExecution // 耐久不足なら終了

                        // 破壊成立
                        weaponItemStack.damageItem(damage, player)
                        breakBlock(world, player, EnumFacing.UP, weaponItemStack, target, world.rand.randomInt(fortune()), false)
                        val blockState = world.getBlockState(target)
                        breakSound = blockState.block.getSoundType(blockState, world, target, player).breakSound
                        count++
                    }
                }

                // 破壊時
                if (count > 0) {
                    breakSound?.let { world.playSound(null, player.posX, player.posY, player.posZ, it, player.soundCategory, 1.0f, 1.0f) } // エフェクト
                    player.cooldownTracker.setCooldown(this@ItemMiragiumToolBase, (getCoolTime(this@magic)).toInt()) // クールタイム
                }

                // エフェクト
                world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.soundCategory, 1.0f, 1.0f)
                player.spawnSweepParticles()

                return EnumActionResult.SUCCESS
            }

            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                spawnParticle(world, selectorRayTrace.item.position, 0xFFFFFF)
                spawnParticleTargets(
                    world,
                    targets.asSequence().toList().map { Pair(Vec3d(it).addVector(0.5, 0.5, 0.5), EnumTargetExecutability.EFFECTIVE) }
                )
            }
        }
    }

    /**
     * このイテレータは破壊処理中に逐次的に呼び出されるパターンと、破壊前に一括で呼び出されるパターンがあります。
     * 内部で必ず[canBreak]による破壊可能判定を行わなければなりません。
     */
    abstract fun iterateTargets(magicArguments: MagicArguments, blockPosBase: BlockPos): Iterator<BlockPos>
    abstract fun getCoolTime(magicArguments: MagicArguments): Double

    @Suppress("SimplifyBooleanWithConstants")
    open fun canBreak(magicArguments: MagicArguments, blockPos: BlockPos) = true
        && magicArguments.world.getBlockState(blockPos).getBlockHardness(magicArguments.world, blockPos) >= 0 // 岩盤であってはならない
        && isEffective(magicArguments.weaponItemStack, magicArguments.world.getBlockState(blockPos)) // 効果的でなければならない
}
