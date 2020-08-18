package miragefairy2019.mod.api.fairystick;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.oredict.OreIngredient;

public class FairyStickCraftRegistry
{

	public static Optional<IFairyStickCraftResult> getResult(Optional<EntityPlayer> oPlayer, World world, BlockPos pos, ItemStack itemStackFairyStick)
	{
		// TODO レシピシステムの設置

		IBlockState blockState = world.getBlockState(pos);

		List<Runnable> listOnCraft = new ArrayList<>();
		List<Runnable> listOnUpdate = new ArrayList<>();

		// 材料の判定
		{

			// ブロック
			if (!blockState.getBlock().isReplaceable(world, pos)) return Optional.empty();
			if (!Blocks.WATER.getDefaultState().getBlock().canPlaceBlockAt(world, pos)) return Optional.empty();
			if (BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.NETHER)) return Optional.empty();
			listOnCraft.add(() -> {
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
				world.setBlockState(pos, Blocks.WATER.getDefaultState(), 2);
			});
			listOnUpdate.add(() -> {
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

			// アイテム
			{
				List<EntityItem> entities = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos));

				Predicate<Predicate<ItemStack>> pPuller = ingredient -> {

					Iterator<EntityItem> iterator = entities.iterator();
					while (iterator.hasNext()) {
						EntityItem entity = iterator.next();
						ItemStack itemStack = entity.getItem();

						if (ingredient.test(itemStack)) {
							listOnCraft.add(() -> {
								itemStack.shrink(1);
								if (itemStack.isEmpty()) world.removeEntity(entity);
							});
							listOnUpdate.add(() -> {
								world.spawnParticle(EnumParticleTypes.SPELL_MOB, entity.posX, entity.posY, entity.posZ, 0, 1, 0);
							});
							iterator.remove();
							return true;
						}

					}

					return false;
				};

				if (!pPuller.test(new OreIngredient("mirageFairyCrystal"))) return Optional.empty();
				if (!pPuller.test(new OreIngredient("mirageFairy2019FairyWaterRank1"))) return Optional.empty();
			}

			// エフェクト
			listOnCraft.add(() -> {
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

		}

		return Optional.of(new IFairyStickCraftResult() {
			@Override
			public void onCraft()
			{
				listOnCraft.forEach(Runnable::run);
			}

			@Override
			public void onUpdate()
			{
				listOnUpdate.forEach(Runnable::run);
			}
		});
	}

}
