package miragefairy2019.mod.modules.ore;

import miragefairy2019.mod.lib.multi.ItemVariantMaterial;

public class ItemVariantMaterialContained extends ItemVariantMaterial
{

	public final String contains;

	public ItemVariantMaterialContained(String registryName, String unlocalizedName, String oreName, String contains)
	{
		super(registryName, unlocalizedName, oreName);
		this.contains = contains;
	}

	public ItemVariantMaterialContained(String registryName, String unlocalizedName, String contains)
	{
		super(registryName, unlocalizedName);
		this.contains = contains;
	}

}
