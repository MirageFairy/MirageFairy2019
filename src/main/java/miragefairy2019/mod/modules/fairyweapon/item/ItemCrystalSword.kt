package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.mod3.magic.api.IMagicHandler
import miragefairy2019.mod3.magic.positive
import miragefairy2019.mod3.skill.EnumMastery
import miragefairy2019.modkt.api.erg.ErgTypes
import miragefairy2019.modkt.api.mana.ManaTypes
import net.minecraft.init.SoundEvents
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory

class ItemCrystalSword : ItemFairyWeaponBase3(ManaTypes.gaia, EnumMastery.closeCombat,
        0.0, 0.0, 0.0, 0.0,
        ErgTypes.slash, ErgTypes.attack, ErgTypes.crystal, ErgTypes.submission) {
    val extraItemDropRate = "extraItemDropRate"({ percent1.positive }) { (getSkillLevel(mastery) / 100.0).coerceIn(0.0, 1.0) }.setVisibility(Companion.EnumVisibility.ALWAYS)

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
