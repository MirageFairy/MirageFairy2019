package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.mod.fairyweapon.CriticalRate
import miragefairy2019.mod.fairyweapon.magic4.MagicArguments
import miragefairy2019.mod.fairyweapon.magic4.boost
import miragefairy2019.mod.fairyweapon.magic4.criticalRate
import miragefairy2019.mod.fairyweapon.magic4.duration2
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.integer
import miragefairy2019.mod.fairyweapon.magic4.percent2
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.fairyweapon.magic4.world
import miragefairy2019.mod.fairyweapon.spawnMagicParticle
import miragefairy2019.mod.skill.Mastery
import miragefairy2019.mod.systems.getErgFactor
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.SoundCategory
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class ItemFireWand : ItemAoeWeaponBase() {
    override fun MagicArguments.getActualDamage(target: EntityLivingBase): Double {
        val a = damage() * damageBoost() // 基礎ダメージ
        val b = a * criticalRate().get(world.rand).coefficient // クリティカル効果
        return b * target.getErgFactor(Erg.FLAME) // エルグ耐性
    }

    val damage = status("damage", { (5.0 + (!Mana.WIND + !Erg.FLAME) / 20.0) * costFactor }, { float2 })
    val damageBoost = status("damageBoost", { 1.0 + !Mastery.magicCombat / 100.0 }, { boost })
    val criticalRate = status("criticalRate", { CriticalRate(0.0, 1.0, 1.0, 2.0, 0.0, 0.0, 0.0, 0.0) }, { criticalRate })

    override val additionalReach = status("additionalReach", { 2.0 + (!Mana.AQUA + !Erg.KINESIS) / 10.0 atMost 30.0 }, { float2 })
    override val radius = status("radius", { 5.0 + (!Mana.GAIA + !Erg.LEVITATE) / 20.0 atMost 10.0 }, { float2 })
    override val maxTargetCount = status("maxTargetCount", { 4 }, { integer })

    override val looting = status("looting", { 0.0 + (!Mana.SHINE + !Erg.FREEZE) / 10.0 }, { float2 })
    override val wear = status("wear", { 1.0 / (1.0 + (!Mana.FIRE + !Erg.WATER) / 20.0) * costFactor }, { percent2 })
    override val coolTime = status("coolTime", { (20.0 * 2) / (1.0 + (!Mana.DARK + !Erg.ENERGY) / 50.0) * costFactor }, { duration2 })

    override fun onActionEffect(a: MagicArguments, world: WorldServer) {
        world.playSound(null, a.player.posX, a.player.posY, a.player.posZ, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 0.5f, 1.0f) // 爆発音
    }

    override fun onPostHit(a: MagicArguments, world: WorldServer, target: EntityLivingBase) {
        target.setFire(5)
    }

    override fun onHitEffect(a: MagicArguments, world: WorldServer, target: EntityLivingBase) {
        spawnMagicParticle(world, a.player, target)  // 射線エフェクト
        world.spawnParticle(
            EnumParticleTypes.FLAME,
            target.posX + (world.rand.nextDouble() - 0.5) * target.width,
            target.posY + world.rand.nextDouble() * target.height,
            target.posZ + (world.rand.nextDouble() - 0.5) * target.width,
            10,
            target.width.toDouble() / 2.0,
            target.height.toDouble() / 2.0,
            target.width.toDouble() / 2.0,
            0.0
        )
    }

    override fun createDamageSource(a: MagicArguments, world: World, player: EntityPlayer) = super.createDamageSource(a, world, player).also { it.setFireDamage() }

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = listOf("右クリックで攻撃") // TRANSLATE
}
