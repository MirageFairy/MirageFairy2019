package miragefairy2019.mod.api.oreseed;

import java.util.function.Predicate;

import mirrg.boron.util.struct.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IGenerationCondition extends Predicate<Tuple<World, BlockPos>>
{

}
