package miragefairy2019.mod.modules.fairyweapon

import miragefairy2019.api.ILootingDamageSource
import net.minecraft.entity.Entity
import net.minecraft.util.EntityDamageSource

class DamageSourceFairyMagic(damageSourceEntity: Entity, private val lootingLevel: Int) :
    EntityDamageSource("indirectMagic", damageSourceEntity), ILootingDamageSource {
    init {
        setDamageBypassesArmor()
        setMagicDamage()
    }

    override fun getLootingLevel() = lootingLevel
}
