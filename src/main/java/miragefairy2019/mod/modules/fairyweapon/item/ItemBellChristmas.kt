package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.mod.formula4.status
import miragefairy2019.mod.magic4.MagicArguments
import miragefairy2019.mod.magic4.duration
import miragefairy2019.mod.magic4.float2
import miragefairy2019.mod.magic4.integer
import miragefairy2019.mod.magic4.percent0
import miragefairy2019.mod.magic4.percent2
import miragefairy2019.mod.magic4.pitch
import miragefairy2019.mod3.erg.api.EnumErgType
import miragefairy2019.api.Mana
import miragefairy2019.mod3.skill.EnumMastery
import mirrg.kotlin.atMost
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.SoundEvents
import net.minecraft.util.SoundCategory
import net.minecraft.world.WorldServer
import kotlin.math.floor
import kotlin.math.pow

class ItemBellChristmas : ItemAoeWeaponBase() {
    override fun MagicArguments.getActualDamage(target: EntityLivingBase) = damage() * damageBoost() * (if (target.isEntityUndead) 1.5 else 1.0)
    val damage = status("damage", { (3.0 + (!Mana.SHINE + !EnumErgType.CHRISTMAS) / 10.0) * costFactor }, { float2 })
    val damageBoost = status("damageBoost", { 1.0 + !EnumMastery.rangedCombat / 100.0 }, { percent0 })

    override val additionalReach = status("additionalReach", { 3.0 + (!Mana.WIND + !EnumErgType.LEVITATE) / 10.0 atMost 30.0 }, { float2 })
    override val radius = status("radius", { 4.0 + (!Mana.GAIA + !EnumErgType.SOUND) / 20.0 atMost 10.0 }, { float2 })
    override val maxTargetCount = status("maxTargetCount", { floor(5.0 + (!Mana.GAIA + !EnumErgType.SPACE) / 10.0).toInt() }, { integer })

    override val looting = status("looting", { 3.0 + (!Mana.DARK + !EnumErgType.SUBMISSION) / 30.0 }, { float2 })
    override val wear = status("wear", { 1.0 / (1.0 + (!Mana.FIRE + !EnumErgType.CRYSTAL) / 20.0) * costFactor }, { percent2 })
    override val coolTime = status("coolTime", { (20.0 * 4) / (1.0 + (!Mana.AQUA + !EnumErgType.ENERGY) / 50.0) * costFactor }, { duration })

    val pitch = status("pitch", { 0.5.pow(costFactor - 1.0) }, { pitch })
    override fun MagicArguments.onActionEffect(world: WorldServer) {
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, pitch().toFloat()) // 鐘の音
    }
}
