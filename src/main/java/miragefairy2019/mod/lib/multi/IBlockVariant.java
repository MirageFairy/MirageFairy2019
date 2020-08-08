package miragefairy2019.mod.lib.multi;

public interface IBlockVariant
{

	public int getMetadata();

	public String getResourceName();

	public String getUnlocalizedName();

	public default String getHarvestTool()
	{
		return "pickaxe";
	}

	public int getHarvestLevel();

}
