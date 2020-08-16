package miragefairy2019.mod.api.oreseed;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import miragefairy2019.mod.lib.WeightedRandom;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RegistryOreSeedDrop
{

	private static List<IOreSeedDropHandler> registry = new ArrayList<>();

	public static void register(IOreSeedDropHandler handler)
	{
		registry.add(handler);
	}

	public static void register(
		EnumOreSeedType type,
		EnumOreSeedShape shape,
		double weight,
		Supplier<IBlockState> block,
		IGenerationCondition... generationConditions)
	{
		register((type2, shape2, world, pos) -> {
			if (type2 != type) return Optional.empty();
			if (shape2 != shape) return Optional.empty();
			for (IGenerationCondition generationCondition : generationConditions) {
				if (!generationCondition.canDrop(type2, shape2, world, pos)) return Optional.empty();
			}
			return Optional.of(new WeightedRandom.Item<>(block, weight));
		});
	}

	public static List<WeightedRandom.Item<Supplier<IBlockState>>> getList(
		EnumOreSeedType type,
		EnumOreSeedShape shape,
		World world,
		BlockPos pos)
	{
		return ISuppliterator.ofIterable(registry)
			.mapIfPresent(h -> h.getDrop(type, shape, world, pos))
			.toList();
	}

	public static Optional<IBlockState> drop(
		EnumOreSeedType type,
		EnumOreSeedShape shape,
		World world,
		BlockPos pos,
		Random random)
	{
		List<WeightedRandom.Item<Supplier<IBlockState>>> list = getList(type, shape, world, pos);
		if (random.nextDouble() < Math.max(1 - WeightedRandom.getTotalWeight(list), 0)) return Optional.empty();
		return WeightedRandom.getRandomItem(random, list).map(s -> s.get());
	}

}
