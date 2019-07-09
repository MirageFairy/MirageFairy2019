package miragefairy2019.mod.lib.multi;

public abstract class ItemVariant
{

	private int metadata = 0;

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
		return "item." + getOreName();
	}

	public abstract String getRegistryName();

	public abstract String getOreName();

}
