package miragefairy2019.mod.modules.fairycrystal;

import static miragefairy2019.mod.modules.fairy.ModuleFairy.FairyTypes.*;

import java.util.List;

import miragefairy2019.mod.api.ApiFairyCrystal;
import miragefairy2019.mod.api.fairycrystal.DropFixed;
import miragefairy2019.mod.api.fairycrystal.IDrop;
import miragefairy2019.mod.api.fairycrystal.IRightClickDrop;
import miragefairy2019.mod.api.fairycrystal.RightClickDrops;
import miragefairy2019.mod.modules.fairy.VariantFairy;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDoublePlant.EnumPlantType;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;

public class LoaderFairyCrystalDrop
{

	public static void loadFairyCrystalDrop()
	{
		List<IRightClickDrop> d = ApiFairyCrystal.dropsFairyCrystal;

		// コモン
		{
			d.add(RightClickDrops.world(r(water[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(lava[0]), (w, p) -> w.provider.isNether()));
			d.add(RightClickDrops.world(r(fire[0]), (w, p) -> w.provider.isNether()));

			d.add(RightClickDrops.world(r(stone[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(dirt[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(sand[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(gravel[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(iron[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(gold[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(diamond[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(emerald[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(redstone[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(lapislazuli[0]), (w, p) -> w.provider.isSurfaceWorld()));

			d.add(RightClickDrops.world(r(spider[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(chicken[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(skeleton[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(zombie[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(creeper[0]), (w, p) -> w.provider.isSurfaceWorld()));

			d.add(RightClickDrops.world(r(wheat[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(lilac[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(apple[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(carrot[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(cactus[0]), (w, p) -> w.provider.isSurfaceWorld()));
		}

		// 限定高確率ドロップ
		{
			d.add(RightClickDrops.fixed(d(air[0], 1)));

			d.add(RightClickDrops.blocks(d(water[0], 0.3), Blocks.WATER, Blocks.FLOWING_WATER));
			d.add(RightClickDrops.blocks(d(lava[0], 0.1), Blocks.LAVA, Blocks.FLOWING_LAVA));
			d.add(RightClickDrops.blocks(d(fire[0], 0.1), Blocks.FIRE));

			d.add(RightClickDrops.world(d(thunder[0], 0.01), (w, p) -> w.provider.isSurfaceWorld() && w.canSeeSky(p) && w.isRainingAt(p) && w.isThundering()));
			d.add(RightClickDrops.world(d(sun[0], 0.0001), (w, p) -> w.provider.isSurfaceWorld() && w.canSeeSky(p) && time(w, 6000, 18000) && !w.isRainingAt(p)));
			d.add(RightClickDrops.world(d(moon[0], 0.0001), (w, p) -> w.provider.isSurfaceWorld() && w.canSeeSky(p) && (time(w, 19000, 24000) || time(w, 0, 5000)) && !w.isRainingAt(p)));
			d.add(RightClickDrops.world(d(star[0], 0.0003), (w, p) -> w.provider.isSurfaceWorld() && w.canSeeSky(p) && (time(w, 19000, 24000) || time(w, 0, 5000)) && !w.isRainingAt(p)));

			d.add(RightClickDrops.blocks(d(stone[0], 0.3), Blocks.STONE, Blocks.COBBLESTONE));
			d.add(RightClickDrops.blocks(d(dirt[0], 0.3), Blocks.DIRT, Blocks.GRASS));
			d.add(RightClickDrops.blocks(d(sand[0], 0.3), Blocks.SAND, Blocks.SANDSTONE, Blocks.RED_SANDSTONE));
			d.add(RightClickDrops.blocks(d(gravel[0], 0.1), Blocks.GRAVEL));
			d.add(RightClickDrops.ores(d(iron[0], 0.1), "ingotIron", "blockIron"));
			d.add(RightClickDrops.ores(d(gold[0], 0.03), "ingotGold", "blockGold"));
			d.add(RightClickDrops.ores(d(diamond[0], 0.01), "gemDiamond", "blockDiamond"));
			d.add(RightClickDrops.ores(d(emerald[0], 0.03), "gemEmerald", "blockEmerald"));
			d.add(RightClickDrops.ores(d(redstone[0], 0.1), "dustRedstone", "blockRedstone"));
			d.add(RightClickDrops.ores(d(lapislazuli[0], 0.1), "gemLapis", "blockLapis"));

			d.add(RightClickDrops.classEntities(d(enderman[0], 0.03), EntityEnderman.class));
			d.add(RightClickDrops.classEntities(d(spider[0], 0.1), EntitySpider.class));
			d.add(RightClickDrops.classEntities(d(enderdragon[0], 0.1), EntityDragon.class));
			d.add(RightClickDrops.classEntities(d(chicken[0], 0.1), EntityChicken.class));
			d.add(RightClickDrops.classEntities(d(skeleton[0], 0.3), EntitySkeleton.class));
			d.add(RightClickDrops.classEntities(d(zombie[0], 0.3), EntityZombie.class));
			d.add(RightClickDrops.classEntities(d(witherskeleton[0], 0.03), EntityWitherSkeleton.class));
			d.add(RightClickDrops.classEntities(d(wither[0], 0.01), EntityWither.class));
			d.add(RightClickDrops.classEntities(d(creeper[0], 0.1), EntityCreeper.class));

			d.add(RightClickDrops.blocks(d(wheat[0], 0.1), Blocks.WHEAT));
			d.add(RightClickDrops.blockStates(d(lilac[0], 0.03), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, EnumPlantType.SYRINGA)));
			d.add(RightClickDrops.items(d(apple[0], 0.1), Items.APPLE, Items.GOLDEN_APPLE));
			d.add(RightClickDrops.items(d(carrot[0], 0.03), Items.CARROT, Items.CARROT_ON_A_STICK, Items.GOLDEN_CARROT));
			d.add(RightClickDrops.blocks(d(cactus[0], 0.1), Blocks.CACTUS));

			d.add(RightClickDrops.blocks(d(torch[0], 0.3), Blocks.TORCH));
			d.add(RightClickDrops.blocks(d(furnace[0], 0.1), Blocks.FURNACE));
			d.add(RightClickDrops.blocks(d(magentaglazedterracotta[0], 0.03), Blocks.MAGENTA_GLAZED_TERRACOTTA));
			d.add(RightClickDrops.items(d(bread[0], 0.1), Items.BREAD));
			d.add(RightClickDrops.items(d(axe[0], 0.1), Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.GOLDEN_AXE, Items.DIAMOND_AXE));
			d.add(RightClickDrops.blocks(d(chest[0], 0.1), Blocks.CHEST));
			d.add(RightClickDrops.blocks(d(craftingtable[0], 0.1), Blocks.CRAFTING_TABLE));
			d.add(RightClickDrops.items(d(potion[0], 0.1), Items.POTIONITEM, Items.LINGERING_POTION, Items.SPLASH_POTION));
			d.add(RightClickDrops.items(d(sword[0], 0.1), Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD));
			d.add(RightClickDrops.blocks(d(dispenser[0], 0.1), Blocks.DISPENSER));

			d.add(RightClickDrops.world(d(daytime[0], 0.001), (w, p) -> time(w, 6000, 18000)));
			d.add(RightClickDrops.world(d(night[0], 0.001), (w, p) -> time(w, 19000, 24000) || time(w, 0, 5000)));
			d.add(RightClickDrops.world(d(morning[0], 0.001), (w, p) -> time(w, 5000, 9000)));
			d.add(RightClickDrops.world(d(fine[0], 0.01), (w, p) -> w.provider.isSurfaceWorld() && w.canSeeSky(p) && !w.isRainingAt(p)));
			d.add(RightClickDrops.world(d(rain[0], 0.01), (w, p) -> w.provider.isSurfaceWorld() && w.canSeeSky(p) && w.isRainingAt(p)));

			d.add(RightClickDrops.biomeTypes(d(plains[0], 0.01), BiomeDictionary.Type.PLAINS));
			d.add(RightClickDrops.biomeTypes(d(forest[0], 0.01), BiomeDictionary.Type.FOREST));
			d.add(RightClickDrops.biomeTypes(d(ocean[0], 0.01), BiomeDictionary.Type.OCEAN));
		}

	}

	private static IDrop r(VariantFairy variantFairy)
	{
		return new DropFixed(variantFairy.createItemStack(), 0.1 * Math.pow(0.1, variantFairy.type.rare - 1));
	}

	private static IDrop d(VariantFairy variantFairy, double weight)
	{
		return new DropFixed(variantFairy.createItemStack(), weight);
	}

	private static boolean time(World world, int min, int max)
	{
		return world.provider.isSurfaceWorld() && min <= (world.getWorldTime() + 6000) % 24000 && (world.getWorldTime() + 6000) % 24000 <= max;
	}

}
