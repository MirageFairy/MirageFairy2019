package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.fairyweapon.damagesource.IDamageSourceLooting;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSource;

public class DamageSourceFairyMagic extends EntityDamageSource implements IDamageSourceLooting
{

	private int lootingLevel;

	public DamageSourceFairyMagic(Entity damageSourceEntity, int lootingLevel)
	{
		super("indirectMagic", damageSourceEntity);
		this.lootingLevel = lootingLevel;
		setDamageBypassesArmor();
		setMagicDamage();
	}

	@Override
	public int getLootingLevel()
	{
		return lootingLevel;
	}

}
