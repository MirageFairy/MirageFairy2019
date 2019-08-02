package miragefairy2019.mod.modules.fairy;

import miragefairy2019.mod.api.fairy.MirageFairyType;
import miragefairy2019.mod.lib.multi.ItemVariant;

public class VariantMirageFairy extends ItemVariant
{

	public final String unlocalizedName;
	public final MirageFairyType type;

	public VariantMirageFairy(String registryName, String oreName, String unlocalizedName, MirageFairyType type)
	{
		super(registryName, oreName);
		this.unlocalizedName = unlocalizedName;
		this.type = type;
	}

	public String getUnlocalizedName()
	{
		return "item." + unlocalizedName;
	}

}
