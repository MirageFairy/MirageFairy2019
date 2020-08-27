package miragefairy2019.mod.modules.ore;

import static miragefairy2019.mod.api.oreseed.EnumOreSeedShape.*;
import static miragefairy2019.mod.api.oreseed.EnumOreSeedType.*;
import static miragefairy2019.mod.api.oreseed.GenerationConditions.*;
import static miragefairy2019.mod.modules.ore.ore.EnumVariantOre1.*;

import java.util.function.Supplier;

import miragefairy2019.mod.api.oreseed.EnumOreSeedShape;
import miragefairy2019.mod.api.oreseed.EnumOreSeedType;
import miragefairy2019.mod.api.oreseed.IGenerationCondition;
import miragefairy2019.mod.api.oreseed.RegistryOreSeedDrop;
import net.minecraft.block.state.IBlockState;
import scala.util.Random;

public class LoaderOreSeedDrop
{

	public static void loadOreSeedDrop()
	{

		r(STONE, POINT, 0.01, () -> ModuleOre.blockOre1.getState(APATITE_ORE));
		r(STONE, POINT, 0.01, () -> ModuleOre.blockOre1.getState(FLUORITE_ORE));
		r(STONE, POINT, 0.01, () -> ModuleOre.blockOre1.getState(SULFUR_ORE), maxY(15));
		r(STONE, POINT, 0.01, () -> ModuleOre.blockOre1.getState(CINNABAR_ORE), maxY(15));
		r(STONE, POINT, 0.01, () -> ModuleOre.blockOre1.getState(MAGNETITE_ORE));
		r(STONE, POINT, 0.01, () -> ModuleOre.blockOre1.getState(MOONSTONE_ORE), minY(40), maxY(50));

		r(STONE, LARGE, 1, () -> ModuleOre.blockOre1.getState(APATITE_ORE), vein(4378, 32, 8, 0.1));
		r(STONE, PYRAMID, 1, () -> ModuleOre.blockOre1.getState(FLUORITE_ORE), vein(5686, 32, 8, 0.1));
		r(STONE, STAR, 1, () -> ModuleOre.blockOre1.getState(SULFUR_ORE), vein(5543, 32, 8, 0.15), maxY(15));
		r(STONE, POINT, 1, () -> ModuleOre.blockOre1.getState(CINNABAR_ORE), vein(4891, 32, 8, 0.15), maxY(15));
		r(STONE, COAL, 1, () -> ModuleOre.blockOre1.getState(MAGNETITE_ORE), vein(7868, 32, 8, 0.1));
		r(STONE, TINY, 1, () -> ModuleOre.blockOre1.getState(MOONSTONE_ORE), vein(4508, 16, 4, 0.1), minY(40), maxY(50));

		r(STONE, POINT, 1, () -> ModuleOre.blockOre1.getState(PYROPE_ORE), vein(5348, 16, 4, 0.05), maxY(50));
		r(STONE, LARGE, 1, () -> ModuleOre.blockOre1.getState(SMITHSONITE_ORE), vein(2894, 32, 8, 0.08), minY(30));
		r(STONE, MEDIUM, 1, () -> ModuleOre.blockOre1.getState(NEPHRITE_ORE), vein(3485, 64, 16, 0.1));

		r(NETHERRACK, LARGE, 0.10, () -> ModuleOre.blockOre1.getState(NETHERRACK_APATITE_ORE), minY(90));
		r(NETHERRACK, PYRAMID, 0.10, () -> ModuleOre.blockOre1.getState(NETHERRACK_FLUORITE_ORE), minY(90));
		r(NETHERRACK, TINY, 0.30, () -> ModuleOre.blockOre1.getState(NETHERRACK_SULFUR_ORE), minY(20), maxY(40));
		r(NETHERRACK, IRON, 0.10, () -> ModuleOre.blockOre1.getState(NETHERRACK_CINNABAR_ORE));
		r(NETHERRACK, COAL, 0.10, () -> ModuleOre.blockOre1.getState(NETHERRACK_MAGNETITE_ORE));
		r(NETHERRACK, TINY, 0.10, () -> ModuleOre.blockOre1.getState(NETHERRACK_MOONSTONE_ORE), maxY(32));

	}

	private static void r(
		EnumOreSeedType type,
		EnumOreSeedShape shape,
		double weight,
		Supplier<IBlockState> block,
		IGenerationCondition... generationConditions)
	{
		RegistryOreSeedDrop.register(type, shape, weight, block, generationConditions);
	}

	private static IGenerationCondition vein(long seed, int horizontalSize, int verticalSize, double rate)
	{
		return (type, shape, world, pos) -> rand(13788169L
			+ seed * 86802673L
			+ div(pos.getX(), horizontalSize) * 84663211L
			+ div(pos.getY(), verticalSize) * 34193609L
			+ div(pos.getZ(), horizontalSize) * 79500227L) < rate;
	}

	private static int div(int a, int b)
	{
		if (a < 0) a -= b - 1;
		return a / b;
	}

	private static double rand(long seed)
	{
		Random random = new Random(seed);
		random.nextDouble();
		random.nextDouble();
		random.nextDouble();
		return random.nextDouble();
	}

}
