package miragefairy2019.mod.common.fairylogdrop;

import miragefairy2019.mod.api.fairylogdrop.IFairyLogDropCondition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class FairyLogDropConditionCanRain implements IFairyLogDropCondition
{

	@Override
	public boolean test(IBlockAccess blockAccess, BlockPos blockPos)
	{
		return blockAccess.getBiome(blockPos).canRain();
	}

	@Override
	public String getLocalizedDescription()
	{
		return "RAINY";
	}

}
