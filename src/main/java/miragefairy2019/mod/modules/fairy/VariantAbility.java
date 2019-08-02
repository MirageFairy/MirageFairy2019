package miragefairy2019.mod.modules.fairy;

import miragefairy2019.mod.lib.Utils;
import miragefairy2019.mod.lib.multi.ItemVariant;

public class VariantAbility extends ItemVariant
{

	public final EnumMirageSphere mirageSphere;

	public VariantAbility(EnumMirageSphere mirageSphere)
	{
		this.mirageSphere = mirageSphere;
	}

	public String getOreName()
	{
		return "mirageSphere" + Utils.toUpperCaseHead(mirageSphere.abilityType.getName());
	}

}
