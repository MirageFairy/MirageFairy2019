package miragefairy2019.mod.modules.ore;

public interface IOreVariant
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
