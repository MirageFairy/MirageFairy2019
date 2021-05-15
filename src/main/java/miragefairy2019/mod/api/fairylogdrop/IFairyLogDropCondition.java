package miragefairy2019.mod.api.fairylogdrop;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IFairyLogDropCondition
{

	public boolean test(IBlockAccess blockAccess, BlockPos blockPos);

	public String getLocalizedDescription();

}
