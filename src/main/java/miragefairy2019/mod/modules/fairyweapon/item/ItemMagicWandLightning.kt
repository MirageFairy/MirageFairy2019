package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.common.magic.MagicSelectorRayTrace
import miragefairy2019.mod.formula4.status
import miragefairy2019.mod.magic4.MagicHandler
import miragefairy2019.mod.magic4.boolean
import miragefairy2019.mod.magic4.criticalRate
import miragefairy2019.mod.magic4.duration
import miragefairy2019.mod.magic4.float2
import miragefairy2019.mod.magic4.magic
import miragefairy2019.mod.magic4.percent0
import miragefairy2019.mod.magic4.percent2
import miragefairy2019.mod.magic4.positive
import miragefairy2019.mod.magic4.world
import miragefairy2019.mod.modules.fairyweapon.critical.CriticalRate
import miragefairy2019.mod.modules.fairyweapon.magic.SelectorEntityRanged
import miragefairy2019.mod3.skill.EnumMastery
import mirrg.kotlin.atMost
import mirrg.kotlin.castOrNull
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.monster.EntityCreeper
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.world.WorldServer
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.ceil
import miragefairy2019.mod3.erg.api.EnumErgType as Erg
import miragefairy2019.mod3.mana.api.EnumManaType as Mana

class ItemMagicWandLightning : ItemFairyWeaponMagic4() {
    val damage = status("damage", { (4.0 + (!Mana.WIND + !Erg.THUNDER) / 10.0) * costFactor }, { float2 })
    val damageBoost = status("damageBoost", { 1.0 + !EnumMastery.magicCombat / 100.0 }, { percent0 })
    val criticalRate = status("criticalRate", { CriticalRate(0.0, 0.0, 0.0, 7.0, 2.0, 1.0, 0.0, 0.0) }, { criticalRate })
    val additionalReach = status("additionalReach", { 5.0 + (!Mana.AQUA + !Erg.LIGHT) / 10.0 atMost 30.0 }, { float2 })
    val radius = status("radius", { 1.0 + (!Mana.GAIA + !Erg.FLAME) / 20.0 atMost 10.0 }, { float2 })
    val looting = status("looting", { 0.0 + (!Mana.SHINE + !Erg.KNOWLEDGE) / 50.0 }, { float2 })
    val wear = status("wear", { 1.0 / (1.0 + (!Mana.FIRE + !Erg.FREEZE) / 20.0) * costFactor }, { percent2 })
    val coolTime = status("coolTime", { 40.0 / (1.0 + (!Mana.DARK + !Erg.ENERGY) / 50.0) * costFactor }, { duration })
    val lightning = status("lightning", { !Erg.THUNDER >= 10.0 }, { boolean.positive })

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = "右クリックで攻撃" // TODO translate

    override fun getMagic() = magic {
        val magicSelectorRayTrace = MagicSelectorRayTrace.createWith(world, player, additionalReach(), EntityLivingBase::class.java) { it != player } // 視線判定
        val magicSelectorPosition = magicSelectorRayTrace.magicSelectorPosition // 視点判定
        val selectorEntityRanged = SelectorEntityRanged(world, magicSelectorRayTrace.position, EntityLivingBase::class.java, { it != player }, radius(), 1) // 対象判定

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
        val target = selectorEntityRanged.effectiveEntities.firstOrNull() ?: return@magic error(0xFF8800, MagicMessage.NO_TARGET) // 対象判定
        if (player.cooldownTracker.hasCooldown(weaponItem)) return@magic error(0xFFFF00, MagicMessage.COOL_TIME) // クールタイム判定

        // 魔法成立
        object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                magicSelectorPosition.doEffect(0xFFFFFF)
                selectorEntityRanged.effect()
                spawnParticleTargets(world, listOf(target), { it.positionVector.addVector(0.0, it.height.toDouble(), 0.0) }, { 0x00FF00 })
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                val actualDamage = damage() * damageBoost() * criticalRate().get(world.rand).coefficient // ダメージ計算

                // 効果
                if (!world.isRemote) {
                    target.attackEntityFrom(DamageSourceFairyMagic(player, world.rand.randomInt(looting())), actualDamage.toFloat()) // ダメージ発生
                    if (lightning()) target.castOrNull<EntityCreeper>()?.onStruckByLightning(EntityLightningBolt(world, target.posX, target.posY, target.posZ, false)) // 匠の帯電
                }

                // 消費
                if (!world.isRemote) {
                    weaponItemStack.damageItem(world.rand.randomInt(wear()), player) // 耐久
                    player.cooldownTracker.setCooldown(this@ItemMagicWandLightning, ceil(coolTime()).toInt()) // クールタイム
                }

                // エフェクト
                if (!world.isRemote) {
                    world.castOrNull<WorldServer>()?.let { spawnDamageParticle(it, target, actualDamage) } // ダメージのエフェクト
                    // TODO クリティカル時のエフェクト
                    world.castOrNull<WorldServer>()?.let { spawnMagicParticle(it, player, target) } // 魔法のエフェクト
                    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.PLAYERS, 0.2f, 1.0f) // 魔法のSE
                }
                if (world.isRemote) player.swingArm(hand) // 腕を振る

                return EnumActionResult.SUCCESS
            }
        }
    }
}
