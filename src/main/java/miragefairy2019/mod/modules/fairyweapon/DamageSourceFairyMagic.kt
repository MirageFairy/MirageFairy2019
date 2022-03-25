package miragefairy2019.mod.modules.fairyweapon

import miragefairy2019.mod3.damagesource.api.IDamageSourceLooting
import net.minecraft.entity.Entity
import net.minecraft.util.EntityDamageSource

class DamageSourceFairyMagic(damageSourceEntity: Entity, private val lootingLevel: Int) :
    EntityDamageSource("indirectMagic", damageSourceEntity), IDamageSourceLooting {
    init {
        setDamageBypassesArmor()
        setMagicDamage()
    }

    override fun getLootingLevel() = lootingLevel
}
