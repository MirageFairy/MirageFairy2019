package miragefairy2019.mod.api.oreseed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import miragefairy2019.mod.lib.WeightedRandom;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RegisterOreSeedDrop
{

	public static Map<EnumOreSeedShape, List<Function<Tuple<World, BlockPos>, Optional<WeightedRandom.Item<Supplier<IBlockState>>>>>> registry = new HashMap<>();

	public static void register(EnumOreSeedShape shape, double weight, Supplier<IBlockState> block, IGenerationCondition... generationConditions)
	{
		registry.compute(shape, (v, l) -> {
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

	private static List<WeightedRandom.Item<Supplier<IBlockState>>> getList(EnumOreSeedShape shape, World world, BlockPos pos)
	{
		List<Function<Tuple<World, BlockPos>, Optional<WeightedRandom.Item<Supplier<IBlockState>>>>> list = RegisterOreSeedDrop.registry.get(shape);
		if (list == null) return new ArrayList<>();
		List<WeightedRandom.Item<Supplier<IBlockState>>> list2 = ISuppliterator.ofIterable(list)
			.mapIfPresent(f -> f.apply(Tuple.of(world, pos)))
			.toList();
		return list2;
	}

	public static Optional<IBlockState> drop(EnumOreSeedShape shape, World world, BlockPos pos, Random random)
	{
		List<WeightedRandom.Item<Supplier<IBlockState>>> list = getList(shape, world, pos);
		if (random.nextDouble() < Math.max(1 - WeightedRandom.getTotalWeight(list), 0)) return Optional.empty();
		return WeightedRandom.getRandomItem(random, list).map(s -> s.get());
	}

}
