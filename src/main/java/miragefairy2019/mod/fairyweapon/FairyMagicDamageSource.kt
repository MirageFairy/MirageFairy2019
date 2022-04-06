package miragefairy2019.mod.fairyweapon

import miragefairy2019.api.ILootingDamageSource
import net.minecraft.entity.Entity
import net.minecraft.util.EntityDamageSource

class FairyMagicDamageSource(damageSourceEntity: Entity, private val lootingLevel: Int) :
    EntityDamageSource("indirectMagic", damageSourceEntity), ILootingDamageSource {
    init {
        setDamageBypassesArmor()
        setMagicDamage()
    }

    override fun getLootingLevel() = lootingLevel
}
