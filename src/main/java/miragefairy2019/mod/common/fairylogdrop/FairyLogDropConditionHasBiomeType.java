package miragefairy2019.mod.common.fairylogdrop;

import miragefairy2019.mod.api.fairylogdrop.IFairyLogDropCondition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class FairyLogDropConditionHasBiomeType implements IFairyLogDropCondition
{

	private final Type biome;

	public FairyLogDropConditionHasBiomeType(Type biome)
	{
		this.biome = biome;
	}

	@Override
	public boolean test(IBlockAccess blockAccess, BlockPos blockPos)
	{
		return BiomeDictionary.hasType(blockAccess.getBiome(blockPos), biome);
	}

	@Override
	public String getLocalizedDescription()
	{
		return biome.getName();
	}

}
