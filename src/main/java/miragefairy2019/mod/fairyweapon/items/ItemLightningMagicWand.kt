package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.mod.fairyweapon.CriticalRate
import miragefairy2019.mod.fairyweapon.magic4.FormulaArguments
import miragefairy2019.mod.fairyweapon.magic4.MagicArguments
import miragefairy2019.mod.fairyweapon.magic4.boolean
import miragefairy2019.mod.fairyweapon.magic4.criticalRate
import miragefairy2019.mod.fairyweapon.magic4.duration
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.percent0
import miragefairy2019.mod.fairyweapon.magic4.percent2
import miragefairy2019.mod.fairyweapon.magic4.positive
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.fairyweapon.magic4.world
import miragefairy2019.mod.fairyweapon.spawnMagicParticle
import miragefairy2019.mod.skill.Mastery
import mirrg.kotlin.hydrogen.atMost
import mirrg.kotlin.hydrogen.castOrNull
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.monster.EntityCreeper
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.SoundCategory
import net.minecraft.world.WorldServer
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class ItemLightningMagicWand : ItemAoeWeaponBase() {
    override fun MagicArguments.getActualDamage(target: EntityLivingBase) = damage() * damageBoost() * criticalRate().get(world.rand).coefficient
    val damage = status("damage", { (5.0 + (!Mana.WIND + !Erg.THUNDER) / 10.0) * costFactor }, { float2 })
    val damageBoost = status("damageBoost", { 1.0 + !Mastery.magicCombat / 100.0 }, { percent0 })
    val criticalRate = status("criticalRate", { CriticalRate(0.0, 0.0, 0.0, 7.0, 2.0, 1.0, 0.0, 0.0) }, { criticalRate })

    override val additionalReach = status("additionalReach", { 5.0 + (!Mana.AQUA + !Erg.LIGHT) / 10.0 atMost 30.0 }, { float2 })
    override val radius = status("radius", { 1.0 + (!Mana.GAIA + !Erg.FLAME) / 20.0 atMost 10.0 }, { float2 })
    override val maxTargetCount: FormulaArguments.() -> Int get() = { 1 }

    override val looting = status("looting", { 0.0 + (!Mana.SHINE + !Erg.KNOWLEDGE) / 10.0 }, { float2 })
    override val wear = status("wear", { 1.0 / (1.0 + (!Mana.FIRE + !Erg.FREEZE) / 20.0) * costFactor }, { percent2 })
    override val coolTime = status("coolTime", { (20.0 * 2) / (1.0 + (!Mana.DARK + !Erg.ENERGY) / 50.0) * costFactor }, { duration })

    override fun MagicArguments.onActionEffect(world: WorldServer) {
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.PLAYERS, 0.2f, 1.0f) // 爆発音
    }

    val lightning = status("lightning", { !Erg.THUNDER >= 10.0 }, { boolean.positive })
    override fun MagicArguments.onHit(world: WorldServer, target: EntityLivingBase) {
        if (lightning()) target.castOrNull<EntityCreeper>()?.onStruckByLightning(EntityLightningBolt(world, target.posX, target.posY, target.posZ, false)) // 匠の帯電
    }

    override fun MagicArguments.onHitEffect(world: WorldServer, target: EntityLivingBase) {
        spawnMagicParticle(world, player, target)  // 射線エフェクト
    }

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = listOf("右クリックで攻撃、雷属性時、クリーパーを帯電") // TODO translate
}
