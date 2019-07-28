package miragefairy2019.mod.modules.fairy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import miragefairy2019.mod.lib.WeightedRandom;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFairyCrystal extends Item
{

	public static List<Drop> drops = new ArrayList<>();

	public static class Drop
	{

		public final ItemStack itemStack;
		public final double weight;

		public Drop(ItemStack itemStack, double weight)
		{
			this.itemStack = itemStack;
			this.weight = weight;
		}

		public boolean canDrop(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
		{
			return true;
		}

	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack itemStackCrystal = player.getHeldItem(hand);
		if (itemStackCrystal.isEmpty()) return EnumActionResult.PASS;

		if (worldIn.isRemote) return EnumActionResult.SUCCESS;

		// ガチャを引く
		List<WeightedRandom.Item<ItemStack>> list = ISuppliterator.ofIterable(drops)
			.filter(d -> d.canDrop(player, worldIn, pos, hand, facing, hitX, hitY, hitZ))
			.map(d -> new WeightedRandom.Item<>(d.itemStack, d.weight))
			.toList();
		if (WeightedRandom.getTotalWeight(list) < 1) {
			list.add(new WeightedRandom.Item<>(ModuleFairy.FairyTypes.air[0].createItemStack(), 1 - WeightedRandom.getTotalWeight(list)));
		}
		Optional<ItemStack> oItemStack = WeightedRandom.getRandomItem(worldIn.rand, list);

		// ガチャが成功した場合、
		if (oItemStack.isPresent()) {
			if (!oItemStack.get().isEmpty()) {

				// ガチャアイテムを消費
				itemStackCrystal.shrink(1);
				player.addStat(StatList.getObjectUseStats(this));

				// 妖精をドロップ
				BlockPos pos2 = pos.offset(facing);
				EntityItem entityitem = new EntityItem(worldIn, pos2.getX() + 0.5, pos2.getY() + 0.5, pos2.getZ() + 0.5, oItemStack.get().copy());
				entityitem.setDefaultPickupDelay();
				worldIn.spawnEntity(entityitem);

			}
		}

		return EnumActionResult.SUCCESS;
	}

}
