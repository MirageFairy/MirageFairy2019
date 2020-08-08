package miragefairy2019.mod.api.ore;

import miragefairy2019.mod.lib.multi.IBlockVariant;

public interface IBlockVariantHarvestable extends IBlockVariant
{

	public default String getHarvestTool()
	{
		return "pickaxe";
	}

	public int getHarvestLevel();

}
