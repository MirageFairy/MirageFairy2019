package miragefairy2019.mod.api.oreseed;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IGenerationCondition
{

	public boolean canDrop(EnumOreSeedType type, EnumOreSeedShape shape, World world, BlockPos pos);

}
