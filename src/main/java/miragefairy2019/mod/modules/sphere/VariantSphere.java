package miragefairy2019.mod.modules.sphere;

import miragefairy2019.mod.lib.Utils;
import miragefairy2019.mod.lib.multi.ItemVariant;

public class VariantSphere extends ItemVariant
{

	public final EnumSphere sphere;

	public VariantSphere(EnumSphere sphere)
	{
		this.sphere = sphere;
	}

	public String getOreName()
	{
		return "mirageFairy2019Sphere" + Utils.toUpperCaseHead(sphere.abilityType.getName());
	}

}
