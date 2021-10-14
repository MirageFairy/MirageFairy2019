package miragefairy2019.mod.modules.fairyweapon.item

import com.google.common.collect.Multimap
import miragefairy2019.mod3.magic.api.IMagicHandler
import miragefairy2019.mod3.magic.positive
import miragefairy2019.mod3.skill.EnumMastery
import miragefairy2019.modkt.api.erg.ErgTypes
import miragefairy2019.modkt.api.mana.ManaTypes
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.init.SoundEvents
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack

class ItemCrystalSword : ItemFairyWeaponBase3(ManaTypes.gaia, EnumMastery.closeCombat,
        0.0, 0.0, 0.0, 0.0,
        ErgTypes.slash, ErgTypes.attack, ErgTypes.crystal, ErgTypes.submission) {
    val extraItemDropRate = "extraItemDropRate"({ percent1.positive }) { (getSkillLevel(mastery) / 100.0).coerceIn(0.0, 1.0) }.setVisibility(Companion.EnumVisibility.ALWAYS)

    // 攻撃力は7（ダイヤ剣+1）固定
    override fun getAttributeModifiers(equipmentSlot: EntityEquipmentSlot, itemStack: ItemStack): Multimap<String?, AttributeModifier?>? {
        val multimap = super.getAttributeModifiers(equipmentSlot, itemStack)
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.name, AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 7.0, 0))
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.name, AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4, 0))
        }
        return multimap
    }

    init {
        magic {
            object : IMagicHandler {
                override fun onItemRightClick(hand: EnumHand?): EnumActionResult {
                    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.PLAYERS, 1.0f, 1.0f)
                    return super.onItemRightClick(hand)
                }
            }
        }
    }
}
