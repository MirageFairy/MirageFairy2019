package miragefairy2019.mod.modules.fairycrystal;

import static miragefairy2019.mod.modules.fairy.ModuleFairy.FairyTypes.*;

import java.util.List;
import java.util.function.Consumer;

import miragefairy2019.mod.api.ApiFairyCrystal;
import miragefairy2019.mod.api.fairy.registry.ApiFairyRegistry;
import miragefairy2019.mod.api.fairycrystal.DropFixed;
import miragefairy2019.mod.api.fairycrystal.IDrop;
import miragefairy2019.mod.api.fairycrystal.IRightClickDrop;
import miragefairy2019.mod.api.fairycrystal.RightClickDrops;
import miragefairy2019.mod.modules.fairy.VariantFairy;
import mirrg.boron.util.struct.Tuple3;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDoublePlant.EnumPlantType;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.oredict.OreIngredient;

public class LoaderFairyCrystalDrop
{

	public static void loadFairyCrystalDrop()
	{
		List<IRightClickDrop> d = ApiFairyCrystal.dropsFairyCrystal;

		// コモン
		{
			d.add(RightClickDrops.world(r(water[0]), (w, p) -> w.provider.isSurfaceWorld()));

			d.add(RightClickDrops.world(r(stone[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(dirt[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(sand[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(gravel[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(iron[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(gold[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(diamond[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(emerald[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(magnetite[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(apatite[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(fluorite[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(sulfur[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(cinnabar[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(moonstone[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(pyrope[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(smithsonite[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(redstone[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(lapislazuli[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(obsidian[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(coal[0]), (w, p) -> w.provider.isSurfaceWorld()));

			d.add(RightClickDrops.world(r(spider[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(chicken[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(skeleton[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(zombie[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(creeper[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(fish[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(cod[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(salmon[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(pufferfish[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(clownfish[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(villager[0]), (w, p) -> w.provider.isSurfaceWorld()));

			d.add(RightClickDrops.world(r(wheat[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(lilac[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(apple[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(carrot[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(cactus[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(spruce[0]), (w, p) -> w.provider.isSurfaceWorld()));
			d.add(RightClickDrops.world(r(seed[0]), (w, p) -> w.provider.isSurfaceWorld()));
		}
		{
			d.add(RightClickDrops.biomeTypes(r(lava[0]), BiomeDictionary.Type.NETHER));
			d.add(RightClickDrops.biomeTypes(r(fire[0]), BiomeDictionary.Type.NETHER));

			d.add(RightClickDrops.biomeTypes(r(glowstone[0]), BiomeDictionary.Type.NETHER));
		}
		{
			d.add(RightClickDrops.biomeTypes(r(enderman[0]), BiomeDictionary.Type.END));
			d.add(RightClickDrops.biomeTypes(r(enderdragon[0]), BiomeDictionary.Type.END));
		}

		// 限定高確率ドロップ
		{
			d.add(RightClickDrops.fixed(d(air[0], 1)));

			d.add(RightClickDrops.blocks(d(water[0], 0.3), Blocks.WATER, Blocks.FLOWING_WATER));
			d.add(RightClickDrops.items(d(water[0], 0.3), Items.WATER_BUCKET));
			d.add(RightClickDrops.blocks(d(lava[0], 0.1), Blocks.LAVA, Blocks.FLOWING_LAVA));
			d.add(RightClickDrops.items(d(lava[0], 0.3), Items.LAVA_BUCKET));
			d.add(RightClickDrops.blocks(d(fire[0], 0.1), Blocks.FIRE));

			d.add(RightClickDrops.world(d(thunder[0], 0.01), (w, p) -> w.provider.isSurfaceWorld() && w.canSeeSky(p) && w.isRainingAt(p) && w.isThundering()));
			d.add(RightClickDrops.world(d(sun[0], 0.0001), (w, p) -> w.provider.isSurfaceWorld() && w.canSeeSky(p) && time(w, 6000, 18000) && !w.isRainingAt(p)));
			d.add(RightClickDrops.world(d(moon[0], 0.0001), (w, p) -> w.provider.isSurfaceWorld() && w.canSeeSky(p) && (time(w, 19000, 24000) || time(w, 0, 5000)) && !w.isRainingAt(p)));
			d.add(RightClickDrops.world(d(star[0], 0.0003), (w, p) -> w.provider.isSurfaceWorld() && w.canSeeSky(p) && (time(w, 19000, 24000) || time(w, 0, 5000)) && !w.isRainingAt(p)));

			d.add(RightClickDrops.blocks(d(stone[0], 0.3), Blocks.STONE, Blocks.COBBLESTONE));
			d.add(RightClickDrops.blocks(d(dirt[0], 0.3), Blocks.DIRT, Blocks.GRASS));
			d.add(RightClickDrops.blocks(d(sand[0], 0.3), Blocks.SAND, Blocks.SANDSTONE, Blocks.RED_SANDSTONE));
			d.add(RightClickDrops.blocks(d(gravel[0], 0.1), Blocks.GRAVEL));
			d.add(RightClickDrops.ores(d(iron[0], 0.1), m("Iron")));
			d.add(RightClickDrops.ores(d(gold[0], 0.03), m("Gold")));
			d.add(RightClickDrops.ores(d(diamond[0], 0.01), m("Diamond")));
			d.add(RightClickDrops.ores(d(emerald[0], 0.03), m("Emerald")));
			d.add(RightClickDrops.ores(d(magnetite[0], 0.1), m("Magnetite")));
			d.add(RightClickDrops.ores(d(apatite[0], 0.03), m("Apatite")));
			d.add(RightClickDrops.ores(d(fluorite[0], 0.01), m("Fluorite")));
			d.add(RightClickDrops.ores(d(sulfur[0], 0.03), m("Sulfur")));
			d.add(RightClickDrops.ores(d(cinnabar[0], 0.01), m("Cinnabar")));
			d.add(RightClickDrops.ores(d(moonstone[0], 0.003), m("Moonstone")));
			d.add(RightClickDrops.ores(d(pyrope[0], 0.01), m("Pyrope")));
			d.add(RightClickDrops.ores(d(smithsonite[0], 0.1), m("Smithsonite")));
			d.add(RightClickDrops.ores(d(redstone[0], 0.1), m("Redstone")));
			d.add(RightClickDrops.ores(d(lapislazuli[0], 0.1), m("Lapis")));
			d.add(RightClickDrops.blocks(d(obsidian[0], 0.1), Blocks.OBSIDIAN));
			d.add(RightClickDrops.ores(d(glowstone[0], 0.1), m("Glowstone")));
			d.add(RightClickDrops.ores(d(glowstone[0], 0.1), "glowstone"));
			d.add(RightClickDrops.ingredients(d(coal[0], 0.1), Ingredient.fromStacks(new ItemStack(Items.COAL))));
			d.add(RightClickDrops.ores(d(coal[0], 0.1), m("Coal")));

			d.add(RightClickDrops.classEntities(d(enderman[0], 0.03), EntityEnderman.class));
			d.add(RightClickDrops.classEntities(d(spider[0], 0.1), EntitySpider.class));
			d.add(RightClickDrops.classEntities(d(enderdragon[0], 0.1), EntityDragon.class));
			d.add(RightClickDrops.classEntities(d(chicken[0], 0.1), EntityChicken.class));
			d.add(RightClickDrops.classEntities(d(skeleton[0], 0.3), EntitySkeleton.class));
			d.add(RightClickDrops.classEntities(d(zombie[0], 0.3), EntityZombie.class));
			d.add(RightClickDrops.classEntities(d(witherskeleton[0], 0.03), EntityWitherSkeleton.class));
			d.add(RightClickDrops.classEntities(d(wither[0], 0.01), EntityWither.class));
			d.add(RightClickDrops.classEntities(d(creeper[0], 0.1), EntityCreeper.class));
			d.add(RightClickDrops.items(d(fish[0], 0.3), Items.FISH));
			d.add(RightClickDrops.ingredients(d(cod[0], 0.1), Ingredient.fromStacks(new ItemStack(Items.FISH, 1, 0))));
			d.add(RightClickDrops.ingredients(d(salmon[0], 0.1), Ingredient.fromStacks(new ItemStack(Items.FISH, 1, 1))));
			d.add(RightClickDrops.ingredients(d(pufferfish[0], 0.03), Ingredient.fromStacks(new ItemStack(Items.FISH, 1, 3))));
			d.add(RightClickDrops.ingredients(d(clownfish[0], 0.03), Ingredient.fromStacks(new ItemStack(Items.FISH, 1, 2))));
			d.add(RightClickDrops.entity(d(villager[0], 0.3), EntityVillager.class, e -> true));
			d.add(RightClickDrops.entity(d(librarian[0], 0.1), EntityVillager.class, e -> e.getProfessionForge().getRegistryName().equals(new ResourceLocation("minecraft:librarian"))));
			d.add(RightClickDrops.items(d(netherstar[0], 0.01), Items.NETHER_STAR));

			d.add(RightClickDrops.blocks(d(wheat[0], 0.1), Blocks.WHEAT, Blocks.HAY_BLOCK));
			d.add(RightClickDrops.blockStates(d(lilac[0], 0.03), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, EnumPlantType.SYRINGA)));
			d.add(RightClickDrops.items(d(apple[0], 0.1), Items.APPLE, Items.GOLDEN_APPLE));
			d.add(RightClickDrops.items(d(carrot[0], 0.03), Items.CARROT, Items.CARROT_ON_A_STICK, Items.GOLDEN_CARROT));
			d.add(RightClickDrops.blocks(d(cactus[0], 0.1), Blocks.CACTUS));
			d.add(RightClickDrops.blockStates(d(spruce[0], 0.1),
				Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE),
				Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE),
				Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE)));
			d.add(RightClickDrops.items(d(seed[0], 0.1), Items.WHEAT_SEEDS));

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
			d.add(RightClickDrops.blocks(d(anvil[0], 0.1), Blocks.ANVIL));
			d.add(RightClickDrops.blocks(d(enchant[0], 0.03), Blocks.ENCHANTING_TABLE));
			d.add(RightClickDrops.items(d(enchant[0], 0.03), Items.ENCHANTED_BOOK));
			d.add(RightClickDrops.ingredients(d(enchant[0], 0.03), itemStack -> itemStack.isItemEnchanted()));
			d.add(RightClickDrops.items(d(brewingstand[0], 0.03), Items.BREWING_STAND));
			d.add(RightClickDrops.blocks(d(brewingstand[0], 0.03), Blocks.BREWING_STAND));
			d.add(RightClickDrops.items(d(hoe[0], 0.3), Items.WOODEN_HOE, Items.STONE_HOE, Items.IRON_HOE, Items.GOLDEN_HOE, Items.DIAMOND_HOE));
			d.add(RightClickDrops.items(d(shield[0], 0.1), Items.SHIELD));
			d.add(RightClickDrops.blocks(d(hopper[0], 0.1), Blocks.HOPPER));

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

	private static String[] m(String material)
	{
		return ISuppliterator.of("ingot", "nugget", "gem", "dust", "dustTiny", "block", "rod", "plate", "ore")
			.map(s -> s + material)
			.toArray(String[]::new);
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

	public static void loadFairyRecipe(Consumer<Tuple3<Ingredient, ItemStack, String>> consumerRecipe)
	{

		// 妖精レジストリー
		ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockDiamond"), diamond[0].type.registryName);
		ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockEmerald"), emerald[0].type.registryName);
		ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockPyrope"), pyrope[0].type.registryName);
		ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockMoonstone"), moonstone[0].type.registryName);
		ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockApatite"), apatite[0].type.registryName);
		ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("obsidian"), obsidian[0].type.registryName);
		ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockFluorite"), fluorite[0].type.registryName);
		ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockCinnabar"), cinnabar[0].type.registryName);
		ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockMagnetite"), magnetite[0].type.registryName);
		ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("glowstone"), glowstone[0].type.registryName);
		ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockSmithsonite"), smithsonite[0].type.registryName);
		ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockLapis"), lapislazuli[0].type.registryName);
		ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockSulfur"), sulfur[0].type.registryName);
		ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockGold"), gold[0].type.registryName);
		ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockRedstone"), redstone[0].type.registryName);
		ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(Ingredient.fromStacks(new ItemStack(Blocks.SAND)), sand[0].type.registryName);
		// TODO ほとんどの妖精とアイテムの関連付けは妖精レジストリーを使う

		new Object() {
			public void run()
			{
				r(stone, b(Blocks.STONE, 0));
				r(dirt, b(Blocks.DIRT, 0));
				r(iron, o("ingotIron"));
				r(diamond, o("gemDiamond"));
				r(magnetite, o("gemMagnetite"));
				r(apatite, o("gemApatite"));
				r(fluorite, o("gemFluorite"));
				r(sulfur, o("gemSulfur"));
				r(cinnabar, o("gemCinnabar"));
				r(moonstone, o("gemMoonstone"));
				r(pyrope, o("gemPyrope"));
				r(smithsonite, o("gemSmithsonite"));
				r(redstone, o("dustRedstone"));
				r(sand, b(Blocks.SAND));
				r(gold, o("ingotGold"));
				r(wheat, i(Items.WHEAT));
				r(lilac, b(Blocks.DOUBLE_PLANT, 1));
				r(torch, b(Blocks.TORCH));
				r(gravel, b(Blocks.GRAVEL));
				r(emerald, o("gemEmerald"));
				r(lapislazuli, o("gemLapislazuli"));
				r(furnace, b(Blocks.FURNACE));
				r(magentaglazedterracotta, b(Blocks.MAGENTA_GLAZED_TERRACOTTA));
				r(bread, i(Items.BREAD));
				r(apple, i(Items.APPLE));
				r(carrot, i(Items.CARROT));
				r(cactus, b(Blocks.CACTUS));
				r(axe, i(Items.IRON_AXE));
				r(chest, b(Blocks.CHEST));
				r(craftingtable, b(Blocks.CRAFTING_TABLE));
				r(potion, i(Items.POTIONITEM));
				r(sword, i(Items.IRON_SWORD));
				r(dispenser, b(Blocks.DISPENSER));
				r(cod, i(Items.FISH, 0));
				r(salmon, i(Items.FISH, 1));
				r(pufferfish, i(Items.FISH, 3));
				r(clownfish, i(Items.FISH, 2));
				r(spruce, b(Blocks.LOG, 1), b(Blocks.SAPLING, 1));
				r(anvil, b(Blocks.ANVIL));
				r(obsidian, b(Blocks.OBSIDIAN));
				r(seed, i(Items.WHEAT_SEEDS));
				r(glowstone, i(Items.GLOWSTONE_DUST), b(Blocks.GLOWSTONE));
				r(coal, i(Items.COAL, 0));
				r(netherstar, i(Items.NETHER_STAR));
				r(brewingstand, i(Items.BREWING_STAND));
				r(hoe, i(Items.IRON_HOE));
				r(shield, i(Items.SHIELD));
				r(hopper, b(Blocks.HOPPER));
			}

			private void r(VariantFairy[] variantFairies, Ingredient... ingredients)
			{
				for (Ingredient ingredient : ingredients) {
					consumerRecipe.accept(Tuple3.of(ingredient, variantFairies[0].createItemStack(), variantFairies[0].type.name));
				}
			}

			private Ingredient i(Item item)
			{
				return i(item, 32767);
			}

			private Ingredient i(Item item, int meta)
			{
				return Ingredient.fromStacks(new ItemStack(item, 1, meta));
			}

			private Ingredient b(Block block)
			{
				return b(block, 32767);
			}

			private Ingredient b(Block block, int meta)
			{
				return Ingredient.fromStacks(new ItemStack(Item.getItemFromBlock(block), 1, meta));
			}

			private Ingredient s(ItemStack itemStack)
			{
				return Ingredient.fromStacks(itemStack);
			}

			private Ingredient o(String ore)
			{
				return new OreIngredient(ore);
			}
		}.run();
	}

}
