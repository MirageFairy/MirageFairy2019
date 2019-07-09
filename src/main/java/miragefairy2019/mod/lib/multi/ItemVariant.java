package miragefairy2019.mod.lib.multi;

public class ItemVariant
{

	public final String registryName;
	public final String oreName;

	private int metadata = 0;

	public ItemVariant(String registryName, String oreName)
	{
		this.registryName = registryName;
		this.oreName = oreName;
	}

	public int getMetadata()
	{
		return metadata;
	}

	public void setMetadata(int metadata)
	{
		this.metadata = metadata;
	}

	public String getUnlocalizedName()
	{
		return "item." + oreName;
	}

}
