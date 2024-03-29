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
import miragefairy2019.mod.fairyweapon.spawnVillagerHappyParticle
import mirrg.kotlin.hydrogen.ceilToInt
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World
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
    open fun onActionEffect(a: MagicArguments, world: WorldServer) = Unit

    /** サーバーワールドでのみ呼び出されます。 */
    open fun onPreHit(a: MagicArguments, world: WorldServer, target: EntityLivingBase) = Unit

    /** サーバーワールドでのみ呼び出されます。 */
    open fun onPostHit(a: MagicArguments, world: WorldServer, target: EntityLivingBase) = Unit

    /** サーバーワールドでのみ呼び出されます。 */
    open fun onKill(a: MagicArguments, world: WorldServer, target: EntityLivingBase) = Unit

    /** サーバーワールドでのみ呼び出されます。 */
    open fun onHitEffect(a: MagicArguments, world: WorldServer, target: EntityLivingBase) = Unit

    /** サーバーワールドでのみ呼び出されます。 */
    open fun createDamageSource(a: MagicArguments, world: World, player: EntityPlayer) = FairyMagicDamageSource(player, world.rand.randomInt(looting(a)))


    override fun getMagic() = magic {
        val rayTraceMagicSelector = MagicSelector.rayTrace(world, player, additionalReach(), EntityLivingBase::class.java) { it != player } // 視線判定
        val cursorMagicSelector = rayTraceMagicSelector.position // 視点判定
        val rangeMagicSelector = cursorMagicSelector.sphere(radius())
        val targetsMagicSelector = rangeMagicSelector.entities(EntityLivingBase::class.java, { it != player && it.health > 0 }, maxTargetCount()) // 対象判定
        val targets = targetsMagicSelector.item.entities

        fun error(color: Int, magicMessage: MagicMessage) = object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(color)
                rangeMagicSelector.item.doEffect()
                targetsMagicSelector.item.doEffect()
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
                cursorMagicSelector.item.doEffect(0xFFFFFF)
                rangeMagicSelector.item.doEffect()
                targetsMagicSelector.item.doEffect()
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
                            onPreHit(this@magic, worldServer, target)
                            if (actualDamage > 0) {
                                target.attackEntityFrom(createDamageSource(this@magic, world, player), actualDamage.toFloat()) // ダメージ発生
                            } else if (actualDamage < 0) {
                                target.heal(-actualDamage.toFloat())
                                spawnVillagerHappyParticle(worldServer, target, count = (-actualDamage.toFloat()).ceilToInt())
                            }
                            onPostHit(this@magic, worldServer, target)
                            if (target.health <= 0) onKill(this@magic, worldServer, target)

                            // ターゲットごとの消費
                            weaponItemStack.damageItem(world.rand.randomInt(wear()), player) // 耐久値の消費

                            // ターゲットごとのエフェクト
                            spawnDamageParticle(worldServer, target, actualDamage)  // ダメージのエフェクト
                            // TODO クリティカル時のエフェクト
                            onHitEffect(this@magic, worldServer, target)

                        }
                    }

                    // 行使ごとの消費
                    player.cooldownTracker.setCooldown(this@ItemAoeWeaponBase, ceil(coolTime()).toInt()) // クールタイム

                    // 行使ごとのエフェクト
                    onActionEffect(this@magic, worldServer)

                } else {

                    // エフェクト
                    player.swingArm(hand) // 腕を振る

                }
                return EnumActionResult.SUCCESS
            }
        }
    }
}
