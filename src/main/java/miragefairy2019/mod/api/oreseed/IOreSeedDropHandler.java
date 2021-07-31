package miragefairy2019.mod.api.oreseed;

import miragefairy2019.mod.lib.WeightedRandom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.function.Supplier;

public interface IOreSeedDropHandler {

    public Optional<WeightedRandom.Item<Supplier<IBlockState>>> getDrop(EnumOreSeedType type, EnumOreSeedShape shape, World world, BlockPos pos);

}
