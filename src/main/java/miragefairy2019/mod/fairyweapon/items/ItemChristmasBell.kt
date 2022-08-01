package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.drop
import miragefairy2019.mod.fairyweapon.magic4.MagicArguments
import miragefairy2019.mod.fairyweapon.magic4.boost
import miragefairy2019.mod.fairyweapon.magic4.duration2
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.integer
import miragefairy2019.mod.fairyweapon.magic4.percent2
import miragefairy2019.mod.fairyweapon.magic4.pitch
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.skill.Mastery
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.Items
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.SoundCategory
import net.minecraft.world.WorldServer
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.floor
import kotlin.math.pow

class ItemChristmasBell : ItemAoeWeaponBase() {
    override fun MagicArguments.getActualDamage(target: EntityLivingBase) = damage() * damageBoost() * (if (target.isEntityUndead) 1.5 else 1.0)
    val damage = status("damage", { (3.0 + (!Mana.SHINE + !Erg.CHRISTMAS) / 10.0) * costFactor }, { float2 })
    val damageBoost = status("damageBoost", { 1.0 + !Mastery.rangedCombat / 100.0 }, { boost })

    override val additionalReach = status("additionalReach", { 3.0 + (!Mana.WIND + !Erg.LEVITATE) / 10.0 atMost 30.0 }, { float2 })
    override val radius = status("radius", { 4.0 + (!Mana.GAIA + !Erg.SOUND) / 20.0 atMost 10.0 }, { float2 })
    override val maxTargetCount = status("maxTargetCount", { floor(5.0 + (!Mana.GAIA + !Erg.SPACE) / 10.0).toInt() }, { integer })

    override val looting = status("looting", { 3.0 + (!Mana.DARK + !Erg.SUBMISSION) / 30.0 }, { float2 })
    override val wear = status("wear", { 1.0 / (1.0 + (!Mana.FIRE + !Erg.CRYSTAL) / 20.0) * costFactor }, { percent2 })
    override val coolTime = status("coolTime", { (20.0 * 4) / (1.0 + (!Mana.AQUA + !Erg.ENERGY) / 50.0) * costFactor }, { duration2 })

    val pitch = status("pitch", { 0.5.pow(costFactor - 1.0) }, { pitch })
    override fun MagicArguments.onActionEffect(world: WorldServer) {
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, pitch().toFloat()) // 鐘の音
    }

    override fun MagicArguments.onKill(world: WorldServer, target: EntityLivingBase) {
        if (target.isEntityUndead) Items.COAL.createItemStack().drop(world, target.positionVector)
    }

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = listOf("右クリックで攻撃、アンデッド撃破時、石炭を入手") // TRANSLATE
}
