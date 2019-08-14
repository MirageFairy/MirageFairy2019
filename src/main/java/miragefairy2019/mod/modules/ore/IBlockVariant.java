package miragefairy2019.mod.modules.ore;

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
