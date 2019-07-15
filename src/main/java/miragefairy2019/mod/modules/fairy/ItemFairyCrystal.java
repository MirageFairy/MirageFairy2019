package miragefairy2019.mod.modules.fairy;

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

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack itemStackCrystal = player.getHeldItem(hand);
		if (itemStackCrystal.isEmpty()) return EnumActionResult.PASS;

		if (worldIn.isRemote) return EnumActionResult.SUCCESS;

		ItemStack itemStackFairy = null;
		{
			int random = worldIn.rand.nextInt(100);
			if (random >= 50) {
				itemStackFairy = ModuleFairy.FairyTypes.air.createItemStack();
			} else if (random >= 20) {
				itemStackFairy = ModuleFairy.FairyTypes.dirt.createItemStack();
			} else if (random >= 5) {
				itemStackFairy = ModuleFairy.FairyTypes.water.createItemStack();
			} else if (random >= 0) {
				itemStackFairy = ModuleFairy.FairyTypes.fire.createItemStack();
			}
		}

		if (itemStackFairy != null) {
			itemStackCrystal.shrink(1);
			player.addStat(StatList.getObjectUseStats(this));

			BlockPos pos2 = pos.offset(facing);
			EntityItem entityitem = new EntityItem(worldIn, pos2.getX() + 0.5, pos2.getY() + 0.5, pos2.getZ() + 0.5, itemStackFairy);
			entityitem.setDefaultPickupDelay();
			worldIn.spawnEntity(entityitem);
		}

		return EnumActionResult.SUCCESS;
	}

}
