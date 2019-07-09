package miragefairy2019.mod.modules.fairy;

import miragefairy2019.mod.lib.multi.ItemVariant;

public class VariantMirageFairy extends ItemVariant
{

	public final MirageFairyType type;

	public VariantMirageFairy(String registryName, String oreName, MirageFairyType type)
	{
		super(registryName, oreName);
		this.type = type;
	}

}
