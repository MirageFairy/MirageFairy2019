package miragefairy2019.mod.modules.oreseed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import miragefairy2019.mod.api.oreseed.IGenerationCondition;
import miragefairy2019.mod.lib.WeightedRandom;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RegisterOreSeedDrop
{

	public static Map<EnumVariantOreSeed, List<Function<Tuple<World, BlockPos>, Optional<WeightedRandom.Item<Supplier<IBlockState>>>>>> registry = new HashMap<>();

	public static void register(EnumVariantOreSeed variant, double weight, Supplier<IBlockState> block, IGenerationCondition... generationConditions)
	{
		registry.compute(variant, (v, l) -> {
			if (l == null) l = new ArrayList<>();
			l.add(t -> {
				for (IGenerationCondition generationCondition : generationConditions) {
					if (!generationCondition.test(t)) return Optional.empty();
				}
				return Optional.of(new WeightedRandom.Item<>(block, weight));
			});
			return l;
		});
	}

}
