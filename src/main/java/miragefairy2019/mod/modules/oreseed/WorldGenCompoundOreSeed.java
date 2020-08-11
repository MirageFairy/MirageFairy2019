package miragefairy2019.mod.modules.oreseed;

import miragefairy2019.mod.modules.oreseed.worldgen.WorldGenOreSeedCube;
import miragefairy2019.mod.modules.oreseed.worldgen.WorldGenOreSeedHorizontal;
import miragefairy2019.mod.modules.oreseed.worldgen.WorldGenOreSeedPoint;
import miragefairy2019.mod.modules.oreseed.worldgen.WorldGenOreSeedPyramid;
import miragefairy2019.mod.modules.oreseed.worldgen.WorldGenOreSeedRing;
import miragefairy2019.mod.modules.oreseed.worldgen.WorldGenOreSeedStar;
import miragefairy2019.mod.modules.oreseed.worldgen.WorldGenOreSeedString;
import miragefairy2019.mod.modules.oreseed.worldgen.WorldGenOreSeedVertical;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.OreGenEvent;

public class WorldGenCompoundOreSeed
{

	private WorldGenerator worldGenOreSeed1;
	private WorldGenerator worldGenOreSeed2;
	private WorldGenerator worldGenOreSeed3;
	private WorldGenerator worldGenOreSeed4;
	private WorldGenerator worldGenOreSeed5;
	private WorldGenerator worldGenOreSeed6;
	private WorldGenerator worldGenOreSeed7;
	private WorldGenerator worldGenOreSeed8;

	private WorldGenerator worldGenOreSeed15;
	private WorldGenerator worldGenOreSeed16;
	private WorldGenerator worldGenOreSeed12;
	private WorldGenerator worldGenOreSeed9;
	private WorldGenerator worldGenOreSeed14;
	private WorldGenerator worldGenOreSeed11;
	private WorldGenerator worldGenOreSeed13;
	private WorldGenerator worldGenOreSeed10;

	public WorldGenCompoundOreSeed()
	{
		worldGenOreSeed1 = new WorldGenMinable(ModuleOreSeed.blockOreSeed.getState(EnumVariantOreSeed.TINY), 5);
		worldGenOreSeed2 = new WorldGenMinable(ModuleOreSeed.blockOreSeed.getState(EnumVariantOreSeed.LAPIS), 7);
		worldGenOreSeed3 = new WorldGenMinable(ModuleOreSeed.blockOreSeed.getState(EnumVariantOreSeed.DIAMOND), 8);
		worldGenOreSeed4 = new WorldGenMinable(ModuleOreSeed.blockOreSeed.getState(EnumVariantOreSeed.IRON), 9);
		worldGenOreSeed5 = new WorldGenMinable(ModuleOreSeed.blockOreSeed.getState(EnumVariantOreSeed.MEDIUM), 12);
		worldGenOreSeed6 = new WorldGenMinable(ModuleOreSeed.blockOreSeed.getState(EnumVariantOreSeed.LARGE), 15);
		worldGenOreSeed7 = new WorldGenMinable(ModuleOreSeed.blockOreSeed.getState(EnumVariantOreSeed.COAL), 17);
		worldGenOreSeed8 = new WorldGenMinable(ModuleOreSeed.blockOreSeed.getState(EnumVariantOreSeed.HUGE), 20);

		worldGenOreSeed15 = new WorldGenOreSeedString(ModuleOreSeed.blockOreSeed.getState(EnumVariantOreSeed.STRING));
		worldGenOreSeed16 = new WorldGenOreSeedHorizontal(ModuleOreSeed.blockOreSeed.getState(EnumVariantOreSeed.HORIZONTAL));
		worldGenOreSeed12 = new WorldGenOreSeedVertical(ModuleOreSeed.blockOreSeed.getState(EnumVariantOreSeed.VERTICAL));
		worldGenOreSeed9 = new WorldGenOreSeedPoint(ModuleOreSeed.blockOreSeed.getState(EnumVariantOreSeed.POINT));
		worldGenOreSeed14 = new WorldGenOreSeedStar(ModuleOreSeed.blockOreSeed.getState(EnumVariantOreSeed.STAR));
		worldGenOreSeed11 = new WorldGenOreSeedRing(ModuleOreSeed.blockOreSeed.getState(EnumVariantOreSeed.RING));
		worldGenOreSeed13 = new WorldGenOreSeedPyramid(ModuleOreSeed.blockOreSeed.getState(EnumVariantOreSeed.PYRAMID));
		worldGenOreSeed10 = new WorldGenOreSeedCube(ModuleOreSeed.blockOreSeed.getState(EnumVariantOreSeed.CUBE));
	}

	public void accept(OreGenEvent.Post event)
	{
		genStandardOre(event, 325 * 7 / 8 / 2 / 4, worldGenOreSeed15, 0, 255);
		genStandardOre(event, 263 * 7 / 9 / 2, worldGenOreSeed16, 0, 255);
		genStandardOre(event, 263 * 7 / 8 / 2, worldGenOreSeed12, 0, 255);
		genStandardOre(event, 263 * 7 / 1 / 2, worldGenOreSeed9, 0, 255);
		genStandardOre(event, 263 * 7 / 13 / 2, worldGenOreSeed14, 0, 255);
		genStandardOre(event, 263 * 7 / 8 / 2, worldGenOreSeed11, 0, 255);
		genStandardOre(event, 263 * 7 / 7 / 2, worldGenOreSeed13, 0, 255);
		genStandardOre(event, 263 * 7 / 8 / 2, worldGenOreSeed10, 0, 255);

		genStandardOre(event, 474 / 2, worldGenOreSeed1, 0, 255);
		genStandardOre(event, 293 / 2, worldGenOreSeed2, 0, 255);
		genStandardOre(event, 272 / 2, worldGenOreSeed3, 0, 255);
		genStandardOre(event, 263 / 2, worldGenOreSeed4, 0, 255);
		genStandardOre(event, 144 / 2, worldGenOreSeed5, 0, 255);
		genStandardOre(event, 120 / 2, worldGenOreSeed6, 0, 255);
		genStandardOre(event, 90 / 2, worldGenOreSeed7, 0, 255);
		genStandardOre(event, 45 / 2, worldGenOreSeed8, 0, 255);
	}

	private void genStandardOre(OreGenEvent.Post event, int count, WorldGenerator generator, int minHeightInclusive, int maxHeightExclusive)
	{
		for (int j = 0; j < count; ++j) {
			generator.generate(event.getWorld(), event.getRand(), event.getPos().add(
				event.getRand().nextInt(16),
				event.getRand().nextInt(maxHeightExclusive - minHeightInclusive) + minHeightInclusive,
				event.getRand().nextInt(16)));
		}
	}

}
