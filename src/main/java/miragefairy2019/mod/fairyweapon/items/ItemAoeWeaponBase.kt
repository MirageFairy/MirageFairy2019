package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.lib.MagicSelector
import miragefairy2019.lib.doEffect
import miragefairy2019.lib.entities
import miragefairy2019.lib.position
import miragefairy2019.lib.rayTrace
import miragefairy2019.lib.sphere
import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.fairyweapon.FairyMagicDamageSource
import miragefairy2019.mod.fairyweapon.MagicMessage
import miragefairy2019.mod.fairyweapon.displayText
import miragefairy2019.mod.fairyweapon.magic4.FormulaArguments
import miragefairy2019.mod.fairyweapon.magic4.MagicArguments
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.magic
import miragefairy2019.mod.fairyweapon.magic4.world
import miragefairy2019.mod.fairyweapon.spawnDamageParticle
import miragefairy2019.mod.fairyweapon.spawnParticleTargets
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


    override fun getMagic() = magic {
        val magicSelectorRayTrace = MagicSelector.rayTrace(world, player, additionalReach(), EntityLivingBase::class.java) { it != player } // 視線判定
        val magicSelectorPosition = magicSelectorRayTrace.position // 視点判定
        val magicSelectorSphere = MagicSelector.sphere(world, magicSelectorRayTrace.item.position, radius())
        val magicSelectorEntities = magicSelectorSphere.entities(EntityLivingBase::class.java, { it != player && it.health > 0 }, maxTargetCount()) // 対象判定
        val targets = magicSelectorEntities.item.entities

        fun error(color: Int, magicMessage: MagicMessage) = object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                magicSelectorPosition.item.doEffect(color)
                magicSelectorSphere.item.doEffect()
                magicSelectorEntities.item.doEffect()
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (!world.isRemote) player.sendStatusMessage(magicMessage.displayText, true)
                return EnumActionResult.FAIL
            }
        }

        if (!hasPartnerFairy) return@magic error(0xFF00FF, MagicMessage.NO_FAIRY) // パートナー妖精判定
        if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@magic error(0xFF0000, MagicMessage.INSUFFICIENT_DURABILITY) // 耐久判定
        if (targets.isEmpty()) return@magic error(0xFF8800, MagicMessage.NO_TARGET) // 対象判定
        if (player.cooldownTracker.hasCooldown(weaponItem)) return@magic error(0xFFFF00, MagicMessage.COOL_TIME) // クールタイム判定

        // 魔法成立
        object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                magicSelectorPosition.item.doEffect(0xFFFFFF)
                magicSelectorSphere.item.doEffect()
                magicSelectorEntities.item.doEffect()
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
