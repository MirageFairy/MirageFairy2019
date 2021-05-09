package miragefairy2019.mod.modules.mirageflower.fairylogdrop.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IFairyLogDropCondition
{

	public boolean test(World world, BlockPos blockPos);

	public String getLocalizedDescription();

}
