package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.common.magic.MagicSelectorRayTrace
import miragefairy2019.mod.magic4.FormulaArguments
import miragefairy2019.mod.magic4.MagicArguments
import miragefairy2019.mod.magic4.MagicHandler
import miragefairy2019.mod.magic4.world
import miragefairy2019.mod.modules.fairyweapon.FairyMagicDamageSource
import miragefairy2019.mod.modules.fairyweapon.MagicMessage
import miragefairy2019.mod.modules.fairyweapon.magic.SelectorEntityRanged
import miragefairy2019.mod.modules.fairyweapon.spawnDamageParticle
import miragefairy2019.mod.modules.fairyweapon.spawnParticleTargets
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.WorldServer
import kotlin.math.ceil

abstract class ItemAoeWeaponBase : ItemFairyWeaponMagic4() {
    abstract fun MagicArguments.getActualDamage(target: EntityLivingBase): Double

    abstract val additionalReach: FormulaArguments.() -> Double
    abstract val radius: FormulaArguments.() -> Double
    abstract val maxTargetCount: FormulaArguments.() -> Int

    abstract val looting: FormulaArguments.() -> Double
    abstract val wear: FormulaArguments.() -> Double
    abstract val coolTime: FormulaArguments.() -> Double

    /** サーバーワールドでのみ呼び出されます。 */
    open fun MagicArguments.onActionEffect(world: WorldServer) = Unit

    /** サーバーワールドでのみ呼び出されます。 */
    open fun MagicArguments.onHit(world: WorldServer, target: EntityLivingBase) = Unit

    /** サーバーワールドでのみ呼び出されます。 */
    open fun MagicArguments.onKill(world: WorldServer, target: EntityLivingBase) = Unit

    /** サーバーワールドでのみ呼び出されます。 */
    open fun MagicArguments.onHitEffect(world: WorldServer, target: EntityLivingBase) = Unit


    override fun getMagic() = miragefairy2019.mod.magic4.magic {
        val magicSelectorRayTrace = MagicSelectorRayTrace.createWith(world, player, additionalReach(), EntityLivingBase::class.java) { it != player } // 視線判定
        val magicSelectorPosition = magicSelectorRayTrace.magicSelectorPosition // 視点判定
        val selectorEntityRanged = SelectorEntityRanged(world, magicSelectorRayTrace.position, EntityLivingBase::class.java, { it != player }, radius(), maxTargetCount()) // 対象判定

        fun error(color: Int, magicMessage: MagicMessage) = object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                magicSelectorPosition.doEffect(color)
                selectorEntityRanged.effect()
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (!world.isRemote) player.sendStatusMessage(magicMessage.displayText, true)
                return EnumActionResult.FAIL
            }
        }

        if (!hasPartnerFairy) return@magic error(0xFF00FF, MagicMessage.NO_FAIRY) // パートナー妖精判定
        if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@magic error(0xFF0000, MagicMessage.INSUFFICIENT_DURABILITY) // 耐久判定
        val targets = selectorEntityRanged.effectiveEntities.take(maxTargetCount()).filter { it.health > 0 }
        if (targets.isEmpty()) return@magic error(0xFF8800, MagicMessage.NO_TARGET) // 対象判定
        if (player.cooldownTracker.hasCooldown(weaponItem)) return@magic error(0xFFFF00, MagicMessage.COOL_TIME) // クールタイム判定

        // 魔法成立
        object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                magicSelectorPosition.doEffect(0xFFFFFF)
                selectorEntityRanged.effect()
                spawnParticleTargets(world, targets, { it.positionVector.addVector(0.0, it.height.toDouble(), 0.0) }, { 0x00FF00 })
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (!world.isRemote) {
                    val worldServer = world as? WorldServer ?: return EnumActionResult.SUCCESS

                    run a@{
                        targets.forEach { target ->

                            // ターゲットごとの消費確認
                            if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@a // 耐久がないなら中断

                            // 効果
                            val actualDamage = getActualDamage(target) // ダメージ計算
                            target.attackEntityFrom(FairyMagicDamageSource(player, world.rand.randomInt(looting())), actualDamage.toFloat()) // ダメージ発生
                            onHit(worldServer, target)
                            if (target.health <= 0) onKill(worldServer, target)

                            // ターゲットごとの消費
                            weaponItemStack.damageItem(world.rand.randomInt(wear()), player) // 耐久値の消費

                            // ターゲットごとのエフェクト
                            spawnDamageParticle(worldServer, target, actualDamage)  // ダメージのエフェクト
                            // TODO クリティカル時のエフェクト
                            onHitEffect(worldServer, target)

                        }
                    }

                    // 行使ごとの消費
                    player.cooldownTracker.setCooldown(this@ItemAoeWeaponBase, ceil(coolTime()).toInt()) // クールタイム

                    // 行使ごとのエフェクト
                    onActionEffect(worldServer)

                } else {

                    // エフェクト
                    player.swingArm(hand) // 腕を振る

                }
                return EnumActionResult.SUCCESS
            }
        }
    }
}
