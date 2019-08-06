package miragefairy2019.mod.lib.multi;

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
