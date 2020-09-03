package miragefairy2019.mod.modules.ore;

import static miragefairy2019.mod.api.oreseed.EnumOreSeedShape.*;
import static miragefairy2019.mod.api.oreseed.EnumOreSeedType.*;
import static miragefairy2019.mod.api.oreseed.GenerationConditions.*;
import static miragefairy2019.mod.modules.ore.Elements.*;
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

	// /effect @p minecraft:night_vision 99999 1
	// /fill ~-120 10 ~ ~120 80 ~ minecraft:air
	// /fill ~-60 ~ ~-60 ~60 ~1 ~60 minecraft:air
	// /fill ~-90 ~ ~-90 ~90 ~ ~90 minecraft:air
	// /fill ~-90 45 ~-90 ~90 45 ~90 minecraft:air
	// /fill ~-90 11 ~-90 ~90 11 ~90 minecraft:air

	public static void loadOreSeedDrop()
	{

		// まばら天然石
		r(STONE, POINT, 0.01, () -> ModuleOre.blockOre1.getState(APATITE_ORE));
		r(STONE, POINT, 0.01, () -> ModuleOre.blockOre1.getState(FLUORITE_ORE));
		r(STONE, POINT, 0.01, () -> ModuleOre.blockOre1.getState(SULFUR_ORE), maxY(15));
		r(STONE, POINT, 0.01, () -> ModuleOre.blockOre1.getState(CINNABAR_ORE), maxY(15));
		r(STONE, POINT, 0.01, () -> ModuleOre.blockOre1.getState(MAGNETITE_ORE));
		r(STONE, POINT, 0.01, () -> ModuleOre.blockOre1.getState(MOONSTONE_ORE), minY(40), maxY(50));

		// 鉱脈天然石
		r2(STONE, LARGE, 1, () -> ModuleOre.blockOre1.getState(APATITE_ORE), vein(97063327, 32, 8, 0.05, FLUORINE, CALCIUM, PHOSPHORUS));
		r2(STONE, PYRAMID, 1, () -> ModuleOre.blockOre1.getState(FLUORITE_ORE), vein(63503821, 32, 8, 0.05, FLUORINE, CALCIUM));
		r2(STONE, STAR, 1, () -> ModuleOre.blockOre1.getState(SULFUR_ORE), vein(34153177, 32, 8, 0.075, SULFUR), maxY(15));
		r2(STONE, POINT, 1, () -> ModuleOre.blockOre1.getState(CINNABAR_ORE), vein(27826567, 32, 8, 0.075, SULFUR, MERCURY), maxY(15));
		r2(STONE, COAL, 1, () -> ModuleOre.blockOre1.getState(MAGNETITE_ORE), vein(16287001, 32, 8, 0.05, FERRUM));
		r2(STONE, TINY, 1, () -> ModuleOre.blockOre1.getState(MOONSTONE_ORE), vein(78750461, 16, 4, 0.05, KALIUM, ALUMINIUM), minY(40), maxY(50));

		// 鉱脈宝石
		r2(STONE, POINT, 0.25, () -> ModuleOre.blockOre1.getState(PYROPE_ORE), vein(39250117, 16, 4, 0.02, MAGNESIUM, ALUMINIUM), maxY(50));
		r2(STONE, LARGE, 0.5, () -> ModuleOre.blockOre1.getState(SMITHSONITE_ORE), vein(32379601, 32, 8, 0.03, ZINC, CARBON), minY(30));
		r2(STONE, MEDIUM, 0.5, () -> ModuleOre.blockOre1.getState(NEPHRITE_ORE), vein(50393467, 64, 16, 0.04, CALCIUM, MAGNESIUM, FERRUM));

		// ネザー鉱石
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

	private static void r2(
		EnumOreSeedType type,
		EnumOreSeedShape shape,
		double weight,
		Supplier<IBlockState> block,
		IGenerationCondition... generationConditions)
	{
		r(type, POINT, weight / 2, block, generationConditions);
		r(type, TINY, weight / 2, block, generationConditions);
		r(type, LAPIS, weight / 2, block, generationConditions);
		r(type, DIAMOND, weight / 2, block, generationConditions);
		r(type, IRON, weight / 2, block, generationConditions);
		r(type, MEDIUM, weight / 2, block, generationConditions);
		r(type, shape, weight, block, generationConditions);
	}

	private static IGenerationCondition vein(long seed, int horizontalSize, int verticalSize, double rate, Element... elements)
	{
		return (type, shape, world, pos) -> {
			int tileX = div(pos.getX(), horizontalSize);
			int tileY = div(pos.getY(), verticalSize);
			int tileZ = div(pos.getZ(), horizontalSize);

			// 成分倍率
			double[] as = new double[elements.length];
			for (int i = 0; i < elements.length; i++) {
				as[i] = randomElement(world.getSeed() * 17566883L + elements[i].seed * 16227457L, elements[i].size, tileX * horizontalSize, tileZ * horizontalSize);
			}

			// 成分倍率の合成
			double a = multiplyElement(as);

			// 鉱石ごとの固有乱数を合成
			double b = rand(13788169L + world.getSeed() * 68640023L + seed * 86802673L + tileX * 84663211L + tileY * 34193609L + tileZ * 79500227L);

			// 出現判定
			return multiplyElement(a, b) < rate;
		};
	}

	/**
	 * 一様分布に従う値を乗算し、更に一様分布になるように補正をかける
	 *
	 * @param as
	 *            要素は1から4まで。
	 */
	private static double multiplyElement(double... as)
	{
		if (as.length == 1) {
			double a = as[0];
			return a;
		} else if (as.length == 2) {
			double a = as[0] * as[1];
			return a - a * log(a);
		} else if (as.length == 3) {
			double a = as[0] * as[1] * as[2];
			return 1 / 2.0 * a * (2 - 2 * log(a) + pow2(log(a)));
		} else if (as.length == 4) {
			double a = as[0] * as[1] * as[2] * as[3];
			return -1 / 6.0 * a * (-6 + 6 * log(a) - 3 * pow2(log(a)) + pow3(log(a)));
		} else {
			throw new IllegalArgumentException();
		}
	}

	private static double log(double a)
	{
		return Math.log(a);
	}

	private static double pow2(double a)
	{
		return a * a;
	}

	private static double pow3(double a)
	{
		return a * a * a;
	}

	/**
	 * 元素の密度を得る
	 *
	 * @return 引数の変動に対して戻り値は一様分布に従います。
	 */
	private static double randomElement(long seed, int size, int x, int z)
	{
		int tileX = div(x, size);
		int tileZ = div(z, size);
		double b00 = randomElementCrossPoint(seed, tileX + 0, tileZ + 0);
		double b01 = randomElementCrossPoint(seed, tileX + 0, tileZ + 1);
		double b10 = randomElementCrossPoint(seed, tileX + 1, tileZ + 0);
		double b11 = randomElementCrossPoint(seed, tileX + 1, tileZ + 1);
		double rateX = (x - tileX * size) / (double) size;
		double rateZ = (z - tileZ * size) / (double) size;
		return k(rateZ, k(rateX, b00 * (1 - rateX) + b10 * rateX) * (1 - rateZ) + k(rateX, b01 * (1 - rateX) + b11 * rateX) * rateZ);
	}

	/**
	 * ブロック座標が所属するチャンク座標を求める
	 */
	private static int div(int a, int b)
	{
		if (a < 0) a -= b - 1;
		return a / b;
	}

	/**
	 * 元素のタイル交点における密度を得る
	 */
	private static double randomElementCrossPoint(long seed, int tileX, int tileZ)
	{
		return rand(49984939L + seed * 15158987L + tileX * 33835717L + tileZ * 46560797L);
	}

	/**
	 * シード付き乱数
	 */
	private static double rand(long seed)
	{
		Random random = new Random(seed);
		random.nextDouble();
		random.nextDouble();
		random.nextDouble();
		return random.nextDouble();
	}

	/**
	 * 2個の一様乱数の間のrateに対応する点における分布を一様分布に補正する関数
	 */
	private static double k(double rate, double x)
	{
		if (rate >= 0.5) rate = 1 - rate;
		return x < 0.5 ? k2(rate, x) : 1 - k2(rate, 1 - x);
	}

	private static double k2(double rate, double x)
	{
		return x < rate
			? (1 / (2 - 2 * rate)) * (x * x / rate)
			: (1 / (2 - 2 * rate)) * (2 * x - rate);
	}

}
