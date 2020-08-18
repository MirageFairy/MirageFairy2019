package miragefairy2019.mod.modules.fairystick;

import static miragefairy2019.mod.lib.Configurator.*;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.fairystick.FairyStickCraftRecipe;
import miragefairy2019.mod.api.fairystick.FairyStickCraftRegistry;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

public class ModuleFairyStick
{

	public static void init(EventRegistryMod erMod)
	{

		// 妖精のステッキ
		item(erMod, ItemFairyStick::new, new ResourceLocation(ModMirageFairy2019.MODID, "fairy_stick"), "fairyStick")
			.bind(setCreativeTab(() -> ApiMain.creativeTab()))
			.bind(onRegisterItem(i -> {
				if (ApiMain.side().isClient()) ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), null));
			}))
			.bind(onCreateItemStack(i -> OreDictionary.registerOre("mirageFairy2019FairyStick", new ItemStack(i))));

		// レシピ登録
		{

			// 水精→水源
			FairyStickCraftRegistry.registerRecipe(new FairyStickCraftRecipe()

				// ブロック
				.add(fairyStickCraft -> {
					World world = fairyStickCraft.getWorld();
					BlockPos pos = fairyStickCraft.getPos();

					if (!fairyStickCraft.getBlockState().getBlock().isReplaceable(world, pos)) return false;
					if (!Blocks.WATER.getDefaultState().getBlock().canPlaceBlockAt(world, pos)) return false;
					if (BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.NETHER)) return false;
					fairyStickCraft.hookOnCraft(() -> {
						world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
						world.setBlockState(pos, Blocks.WATER.getDefaultState(), 2);
					});
					fairyStickCraft.hookOnUpdate(() -> {
						for (int i = 0; i < 5; i++) {
							world.spawnParticle(
								EnumParticleTypes.SPELL_MOB,
								pos.getX() + world.rand.nextDouble(),
								pos.getY() + world.rand.nextDouble(),
								pos.getZ() + world.rand.nextDouble(),
								0.5 + world.rand.nextDouble() * 0.5,
								0.5 + world.rand.nextDouble() * 0.5,
								0.5 + world.rand.nextDouble() * 0.5);
						}
					});

					return true;
				})

				// アイテム
				.add(fairyStickCraft -> {
					World world = fairyStickCraft.getWorld();
					BlockPos pos = fairyStickCraft.getPos();
					List<EntityItem> entities = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos));

					Predicate<Predicate<ItemStack>> pPuller = ingredient -> {

						Iterator<EntityItem> iterator = entities.iterator();
						while (iterator.hasNext()) {
							EntityItem entity = iterator.next();
							ItemStack itemStack = entity.getItem();

							if (ingredient.test(itemStack)) {
								fairyStickCraft.hookOnCraft(() -> {
									itemStack.shrink(1);
									if (itemStack.isEmpty()) world.removeEntity(entity);
								});
								fairyStickCraft.hookOnUpdate(() -> {
									world.spawnParticle(EnumParticleTypes.SPELL_MOB, entity.posX, entity.posY, entity.posZ, 0, 1, 0);
								});
								iterator.remove();
								return true;
							}

						}

						return false;
					};

					if (!pPuller.test(new OreIngredient("mirageFairyCrystal"))) return false;
					if (!pPuller.test(new OreIngredient("mirageFairy2019FairyWaterRank1"))) return false;

					return true;
				})

				// エフェクト
				.add(fairyStickCraft -> {
					World world = fairyStickCraft.getWorld();
					BlockPos pos = fairyStickCraft.getPos();

					fairyStickCraft.hookOnCraft(() -> {
						world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BELL, SoundCategory.PLAYERS, 0.2F, 1.0F);
						for (int i = 0; i < 10; i++) {
							world.spawnParticle(
								EnumParticleTypes.SPELL_MOB,
								pos.getX() + world.rand.nextDouble(),
								pos.getY() + world.rand.nextDouble(),
								pos.getZ() + world.rand.nextDouble(),
								0.5 + world.rand.nextDouble() * 0.5,
								0.5 + world.rand.nextDouble() * 0.5,
								0.5 + world.rand.nextDouble() * 0.5);
						}
					});
					return true;
				})

			);

		}

	}

}
