package miragefairy2019.mod.modules.fairycrystal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import miragefairy2019.mod.api.ApiFairyCrystal;
import miragefairy2019.mod.api.fairycrystal.IDrop;
import miragefairy2019.mod.lib.WeightedRandom;
import miragefairy2019.mod.modules.fairy.ModuleFairy;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class ItemFairyCrystal extends Item
{

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack itemStackCrystal = player.getHeldItem(hand);
		if (itemStackCrystal.isEmpty()) return EnumActionResult.PASS;

		if (!player.isSneaking()) {
			if (world.isRemote) return EnumActionResult.SUCCESS;

			// ガチャを引く
			Optional<ItemStack> oItemStack = drop(player, world, pos, hand, facing, hitX, hitY, hitZ);

			// ガチャが成功した場合、
			if (oItemStack.isPresent()) {
				if (!oItemStack.get().isEmpty()) {

					// ガチャアイテムを消費
					itemStackCrystal.shrink(1);
					player.addStat(StatList.getObjectUseStats(this));

					// 妖精をドロップ
					BlockPos pos2 = pos.offset(facing);
					EntityItem entityitem = new EntityItem(world, pos2.getX() + 0.5, pos2.getY() + 0.5, pos2.getZ() + 0.5, oItemStack.get().copy());
					entityitem.setNoPickupDelay();
					world.spawnEntity(entityitem);

				}
			}

			return EnumActionResult.SUCCESS;
		} else {
			if (world.isRemote) return EnumActionResult.SUCCESS;

			// ガチャリスト取得
			List<WeightedRandom.Item<ItemStack>> dropTable = getDropTable(player, world, pos, hand, facing, hitX, hitY, hitZ);

			// 表示
			List<String> lines = new ArrayList<>();
			lines.add("---- " + itemStackCrystal.getDisplayName() + " ----");
			double totalWeight = WeightedRandom.getTotalWeight(dropTable);
			for (WeightedRandom.Item<ItemStack> item : ISuppliterator.ofIterable(dropTable)
				.sortedObj(i -> i.item.getDisplayName())
				.sortedDouble(i -> i.weight)) {
				lines.add("" + String.format("%f%%", 100 * item.weight / totalWeight) + ": " + item.item.getDisplayName());
			}
			player.sendStatusMessage(new TextComponentString(ISuppliterator.ofIterable(lines)
				.join("\n")), false);

			return EnumActionResult.SUCCESS;
		}

	}

	public static Optional<ItemStack> drop(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{

		// ガチャリスト取得
		List<WeightedRandom.Item<ItemStack>> dropTable = getDropTable(player, world, pos, hand, facing, hitX, hitY, hitZ);

		// ガチャを引く
		Optional<ItemStack> oItemStack = WeightedRandom.getRandomItem(world.rand, dropTable);

		return oItemStack;
	}

	private static List<WeightedRandom.Item<ItemStack>> getDropTable(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		BlockPos pos2 = world.getBlockState(pos).isFullBlock() ? pos.offset(facing) : pos;

		Set<Block> blocks = new HashSet<>();
		Set<Tuple<IBlockState, BlockPos>> blockStates = new HashSet<>();
		Set<Item> items = new HashSet<>();
		Set<ItemStack> itemStacks = new HashSet<>();
		Set<Biome> biomes = new HashSet<>();
		Set<BiomeDictionary.Type> biomeTypes = new HashSet<>();

		// ワールドブロック
		for (int xi = -2; xi <= 2; xi++) {
			for (int yi = -2; yi <= 2; yi++) {
				for (int zi = -2; zi <= 2; zi++) {
					BlockPos pos3 = pos.add(xi, yi, zi);
					IBlockState blockState = world.getBlockState(pos3);
					blockStates.add(Tuple.of(blockState, pos3));
					blocks.add(blockState.getBlock());
					TileEntity tileEntity = world.getTileEntity(pos3);
					if (tileEntity instanceof IInventory) {
						for (int i = 0; i < ((IInventory) tileEntity).getSizeInventory(); i++) {
							ItemStack itemStack = ((IInventory) tileEntity).getStackInSlot(i);
							if (!itemStack.isEmpty()) {
								itemStacks.add(itemStack);
								items.add(itemStack.getItem());
								Block block = Block.getBlockFromItem(itemStack.getItem());
								if (block != Blocks.AIR) blocks.add(block);
							}
						}
					}
				}
			}
		}

		// インベントリ
		for (ItemStack itemStack : player.inventory.mainInventory) {
			if (!itemStack.isEmpty()) {
				itemStacks.add(itemStack);
				items.add(itemStack.getItem());
				Block block = Block.getBlockFromItem(itemStack.getItem());
				if (block != Blocks.AIR) blocks.add(block);
			}
		}
		for (ItemStack itemStack : player.inventory.armorInventory) {
			if (!itemStack.isEmpty()) {
				itemStacks.add(itemStack);
				items.add(itemStack.getItem());
				Block block = Block.getBlockFromItem(itemStack.getItem());
				if (block != Blocks.AIR) blocks.add(block);
			}
		}
		for (ItemStack itemStack : player.inventory.offHandInventory) {
			if (!itemStack.isEmpty()) {
				itemStacks.add(itemStack);
				items.add(itemStack.getItem());
				Block block = Block.getBlockFromItem(itemStack.getItem());
				if (block != Blocks.AIR) blocks.add(block);
			}
		}

		// バイオーム
		{
			Biome biome = world.getBiome(pos2);
			biomes.add(biome);
			biomeTypes.addAll(BiomeDictionary.getTypes(biome));
		}

		// エンティティ
		List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(
			player.posX - 10,
			player.posY - 10,
			player.posZ - 10,
			player.posX + 10,
			player.posY + 10,
			player.posZ + 10));
		Set<Class<? extends Entity>> classEntities = new HashSet<>();
		for (Entity entity : entities) {
			classEntities.add(entity.getClass());
		}

		// リスト作成
		List<WeightedRandom.Item<ItemStack>> dropTable = ISuppliterator.ofIterable(ApiFairyCrystal.dropsFairyCrystal)
			.mapIfNotNull(d -> {
				IDrop drop = d.getDrop();

				if (d.testUseItem(player, world, pos, hand, facing, hitX, hitY, hitZ)) return drop;
				if (d.testWorld(world, pos2)) return drop;

				for (Block block : blocks) {
					if (d.testBlock(block)) return drop;
				}
				for (Tuple<IBlockState, BlockPos> blockState : blockStates) {
					if (d.testBlockState(world, blockState.y, blockState.x)) return drop;
				}
				for (Item item : items) {
					if (d.testItem(item)) return drop;
				}
				for (ItemStack itemStack : itemStacks) {
					if (d.testItemStack(itemStack)) return drop;
				}
				for (Biome biome : biomes) {
					if (d.testBiome(biome)) return drop;
				}
				for (BiomeDictionary.Type biomeType : biomeTypes) {
					if (d.testBiomeType(biomeType)) return drop;
				}
				for (Class<? extends Entity> classEntity : classEntities) {
					if (d.testClassEntity(classEntity)) return drop;
				}
				for (Entity entity : entities) {
					if (d.testEntity(entity)) return drop;
				}

				return null;
			})
			.map(d -> new WeightedRandom.Item<>(d.getItemStack(), d.getWeight()))
			.toList();
		dropTable = WeightedRandom.unique(dropTable, (a, b) -> ItemStack.areItemStacksEqualUsingNBTShareTag(a, b));

		// 1に満たない場合はairを入れて詰める
		double totalWeight = WeightedRandom.getTotalWeight(dropTable);
		if (totalWeight < 1) dropTable.add(new WeightedRandom.Item<>(ModuleFairy.FairyTypes.air[0].createItemStack(), 1 - totalWeight));

		return dropTable;
	}

}
