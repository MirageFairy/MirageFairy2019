package miragefairy2019.mod.modules.fairycrystal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import miragefairy2019.mod.lib.UtilsMinecraft;
import miragefairy2019.mod.lib.WeightedRandom;
import miragefairy2019.mod.lib.multi.ItemMulti;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemFairyCrystal extends ItemMulti<VariantFairyCrystal>
{

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack itemStackCrystal = player.getHeldItem(hand);
		if (itemStackCrystal.isEmpty()) return EnumActionResult.PASS;

		VariantFairyCrystal variant = getVariant(itemStackCrystal).orElse(null);
		if (variant == null) return EnumActionResult.PASS;

		if (!player.isSneaking()) {
			if (world.isRemote) return EnumActionResult.SUCCESS;

			// ガチャを引く
			Optional<ItemStack> oItemStack = variant.getDropper().drop(player, world, pos, hand, facing, hitX, hitY, hitZ);

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
			List<WeightedRandom.Item<ItemStack>> dropTable = variant.getDropper().getDropTable(player, world, pos, hand, facing, hitX, hitY, hitZ);

			// 表示
			List<String> lines = new ArrayList<>();
			lines.add("===== " + itemStackCrystal.getDisplayName() + " (" + (world.isRemote ? "Server" : "Client") + ") =====");
			double totalWeight = WeightedRandom.getTotalWeight(dropTable);
			for (WeightedRandom.Item<ItemStack> item : ISuppliterator.ofIterable(dropTable)
				.sortedObj(i -> i.item.getDisplayName())
				.sortedDouble(i -> i.weight)) {
				lines.add("" + String.format("%f%%", 100 * item.weight / totalWeight) + ": " + item.item.getDisplayName());
			}
			lines.add("====================");
			player.sendStatusMessage(new TextComponentString(ISuppliterator.ofIterable(lines)
				.join("\n")), false);

			return EnumActionResult.SUCCESS;
		}

	}

	@Override
	public String getItemStackDisplayName(ItemStack itemStack)
	{
		VariantFairyCrystal variant = getVariant(itemStack).orElse(null);
		if (variant == null) return UtilsMinecraft.translateToLocal(getUnlocalizedName() + ".name").trim();
		return UtilsMinecraft.translateToLocalFormatted("item." + variant.unlocalizedName + ".name").trim();
	}

}
