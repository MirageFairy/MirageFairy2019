package miragefairy2019.mod.modules.ore.material;

import miragefairy2019.mod.lib.multi.IBlockVariant;
import net.minecraft.block.SoundType;

public interface IBlockVariantMaterials extends IBlockVariant
{

	public default String getHarvestTool()
	{
		return "pickaxe";
	}

	public int getHarvestLevel();

	public int getBurnTime();

	public SoundType getSoundType();

	public boolean isFallable();

}
