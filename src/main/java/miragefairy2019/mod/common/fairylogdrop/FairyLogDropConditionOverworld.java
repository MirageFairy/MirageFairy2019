package miragefairy2019.mod.common.fairylogdrop;

import miragefairy2019.mod.api.fairylogdrop.IFairyLogDropCondition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.BiomeDictionary;

public class FairyLogDropConditionOverworld implements IFairyLogDropCondition
{

	@Override
	public boolean test(IBlockAccess blockAccess, BlockPos blockPos)
	{
		if (BiomeDictionary.hasType(blockAccess.getBiome(blockPos), BiomeDictionary.Type.NETHER)) return false;
		if (BiomeDictionary.hasType(blockAccess.getBiome(blockPos), BiomeDictionary.Type.END)) return false;
		return true;
	}

	@Override
	public String getLocalizedDescription()
	{
		return "OVERWORLD";
	}

}
